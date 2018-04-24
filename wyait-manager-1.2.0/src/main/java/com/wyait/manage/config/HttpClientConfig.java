package com.wyait.manage.config;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

/**
 * @项目名称：wyait-manage
 * @包名：com.wyait.manage.config
 * @类描述：
 * @创建人：wyait
 * @创建时间：2018-01-11 9:13
 * @version：V1.0
 */
@Configuration
public class HttpClientConfig  {
	private static final Logger logger = LoggerFactory
			.getLogger(ShiroConfig.class);
	/**
	 * 连接池最大连接数
	 */
	@Value("${httpclient.config.connMaxTotal}")
	private int connMaxTotal = 20;

	/**
	 *
	 */
	@Value("${httpclient.config.maxPerRoute}")
	private int maxPerRoute = 20;

	/**
	 * 连接存活时间，单位为s
	 */
	@Value("${httpclient.config.timeToLive}")
	private int timeToLive = 10;

	/**
	 * 配置连接池
	 * @return
	 */
	@Bean(name="poolingClientConnectionManager")
	public PoolingHttpClientConnectionManager poolingClientConnectionManager(){
		PoolingHttpClientConnectionManager poolHttpcConnManager = new PoolingHttpClientConnectionManager(60, TimeUnit.SECONDS);
		// 最大连接数
		poolHttpcConnManager.setMaxTotal(this.connMaxTotal);
		// 路由基数
		poolHttpcConnManager.setDefaultMaxPerRoute(this.maxPerRoute);
		return poolHttpcConnManager;
	}

	@Value("${httpclient.config.connectTimeout}")
	private int connectTimeout = 3000;

	@Value("${httpclient.config.connectRequestTimeout}")
	private int connectRequestTimeout = 2000;

	@Value("${httpclient.config.socketTimeout}")
	private int socketTimeout = 3000;

	/**
	 * 设置请求配置
	 * @return
	 */
	@Bean
	public RequestConfig config(){
		return RequestConfig.custom()
				.setConnectionRequestTimeout(this.connectRequestTimeout)
				.setConnectTimeout(this.connectTimeout)
				.setSocketTimeout(this.socketTimeout)
				.build();
	}

	@Value("${httpclient.config.retryTime}")// 此处建议采用@ConfigurationProperties(prefix="httpclient.config")方式，方便复用
	private int retryTime;

	/**
	 * 重试策略
	 * @return
	 */
	@Bean
	public HttpRequestRetryHandler httpRequestRetryHandler() {
		// 请求重试
		final int retryTime = this.retryTime;
		return new HttpRequestRetryHandler() {
			public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
				// Do not retry if over max retry count,如果重试次数超过了retryTime,则不再重试请求
				if (executionCount >= retryTime) {
					return false;
				}
				// 服务端断掉客户端的连接异常
				if (exception instanceof NoHttpResponseException) {
					return true;
				}
				// time out 超时重试
				if (exception instanceof InterruptedIOException) {
					return true;
				}
				// Unknown host
				if (exception instanceof UnknownHostException) {
					return false;
				}
				// Connection refused
				if (exception instanceof ConnectTimeoutException) {
					return false;
				}
				// SSL handshake exception
				if (exception instanceof SSLException) {
					return false;
				}
				HttpClientContext clientContext = HttpClientContext.adapt(context);
				HttpRequest request = clientContext.getRequest();
				if (!(request instanceof HttpEntityEnclosingRequest)) {
					return true;
				}
				return false;
			}
		};
	}

	/**
	 * 创建httpClientBuilder对象
	 * @param httpClientConnectionManager
	 * @return
	 */
	@Bean(name = "httpClientBuilder")
	public HttpClientBuilder getHttpClientBuilder(@Qualifier("poolingClientConnectionManager")PoolingHttpClientConnectionManager httpClientConnectionManager){

		return HttpClients.custom().setConnectionManager(httpClientConnectionManager)
				.setRetryHandler(this.httpRequestRetryHandler())
				//.setKeepAliveStrategy(connectionKeepAliveStrategy())
				//.setRoutePlanner(defaultProxyRoutePlanner())
				.setDefaultRequestConfig(this.config());

	}

	/**
	 * 自动释放连接
	 * @param httpClientBuilder
	 * @return
	 */
	@Bean
	public CloseableHttpClient getCloseableHttpClient(@Qualifier("httpClientBuilder") HttpClientBuilder httpClientBuilder){
		return httpClientBuilder.build();
	}

	/*// 代理的host地址
	@Value("${httpclient.config.proxyhost}")
	private String proxyHost;

	// 代理的端口号
	@Value("${httpclient.config.proxyPort}")
	private int proxyPort = 8080;

	*//**
	 * HttpClient不仅支持简单的直连、复杂的路由策略以及代理。
	 * </br>HttpRoutePlanner是基于http上下文情况下，客户端到服务器的路由计算策略，一般没有代理的话，就不用设置这个东西。
	 * </br>这里有一个很关键的概念—Route：在HttpClient中，一个Route指 运行环境机器->目标机器host的一条线路，
	 * </br>也就是如果目标url的host是同一个，那么它们的route也是一样的
	 * @return
	 *//*
	@Bean
	public DefaultProxyRoutePlanner defaultProxyRoutePlanner(){
		HttpHost proxy = new HttpHost(this.proxyHost, this.proxyPort);
		return new DefaultProxyRoutePlanner(proxy);
	}*/


	/*@Value("${httpclient.config.keepAliveTime}")
	private int keepAliveTime = 30;

	*//**
	 * 连接保持策略:长连接并不使用于所有的情况，尤其现在的系统，大都是部署在多台服务器上，且具有负载均衡的功能，
	 * </br>如果我们在访问的时候，一直保持长连接，一旦那台服务器挂了，就会影响客户端，
	 * </br>同时也不能充分的利用服务端的负载均衡的特性，反而短连接更有利一些，这些需要根据具体的需求来定，而不是一言概括。
	 * @return
	 *//*
	@Bean("connectionKeepAliveStrategy")
	public ConnectionKeepAliveStrategy connectionKeepAliveStrategy() {
		return new ConnectionKeepAliveStrategy() {

			public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
				// Honor 'keep-alive' header
				HeaderElementIterator it = new BasicHeaderElementIterator(
						response.headerIterator(HTTP.CONN_KEEP_ALIVE));
				while (it.hasNext()) {
					HeaderElement he = it.nextElement();
					String param = he.getName();
					String value = he.getValue();
					if (value != null && param.equalsIgnoreCase("timeout")) {
						try {
							return Long.parseLong(value) * 1000;
						} catch (NumberFormatException ignore) {
						}
					}
				}
				return 30 * 1000;
			}
		};
	}*/

}
