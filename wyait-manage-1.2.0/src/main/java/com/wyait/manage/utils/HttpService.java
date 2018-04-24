package com.wyait.manage.utils;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @项目名称：common
 * @类名称：ApiService
 * @类描述：负责和外部接口对接，发起http请求
 * @创建人：wyait
 * @创建时间：2015年10月13日 下午2:55:09 
 * @version：1.0.0
 */
@Service
public class HttpService {

	@Autowired(required = false)
	private CloseableHttpClient httpClient;

	@Autowired(required = false)
	private RequestConfig requestConfig;

	/**
	 *
	 * @描述：发送不带参数的GET请求，返回String类型数据
	 * @创建人：wyait
	 * @创建时间：2015年10月13日 下午2:55:42
	 * @param url 请求地址
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String doGetToString(String url) throws IOException {
		HttpGet httpGet = new HttpGet(url);
		httpGet.setConfig(requestConfig);
		CloseableHttpResponse response = null;

		try {
			response = httpClient.execute(httpGet);
			if (response.getStatusLine().getStatusCode() == 200) {
				// 响应成功，返回数据
				return EntityUtils.toString(response.getEntity(), "UTF-8");
			}
		} finally {
			// 关闭请求，释放资源
			if (response != null) {
				response.close();
			}
		}
		return null;
	}

	/**
	 *
	 * @描述：带参数的GET请求，返回String类型数据
	 * @创建人：wyait
	 * @创建时间：2015年10月13日 下午2:56:06
	 * @param url 请求地址
	 * @param param 请求参数
	 * @return
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String doGetToString(String url, Map<String, Object> param)
			throws URISyntaxException,IOException {

		// 定义参数
		URIBuilder uriBuilder = new URIBuilder(url);
		for (Map.Entry<String, Object> entry : param.entrySet()) {
			uriBuilder
					.setParameter(entry.getKey(), entry.getValue().toString());
		}
		return doGetToString(uriBuilder.build().toString());
	}

	/**
	 *
	 * @描述：执行DoGET请求，返回HttpResult
	 * @创建人：wyait
	 * @创建时间：2015年10月13日 下午2:56:28
	 * @param url 请求地址
	 * @return 如果响应是200，返回具体的响应内容，其他响应返回null
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public HttpResult doGet(String url) throws
			IOException {
		// 创建http GET请求
		HttpGet httpGet = new HttpGet(url);
		httpGet.setConfig(this.requestConfig);
		CloseableHttpResponse response = null;
		try {
			// 执行请求
			response = httpClient.execute(httpGet);
			HttpResult result = new HttpResult();
			result.setCode(response.getStatusLine().getStatusCode());
			if (response.getEntity() != null) {
				result.setBody(EntityUtils.toString(response.getEntity(),
						"UTF-8"));
			}
			return result;
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}

	/**
	 *
	 * @描述：带有参数的GET请求，返回HttpResult
	 * @创建人：wyait
	 * @创建时间：2015年10月13日 下午2:57:08
	 * @param url 请求地址
	 * @param param 请求参数
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public HttpResult doGet(String url, Map<String, Object> param)
			throws IOException, URISyntaxException {
		// 定义请求的参数
		URIBuilder uriBuilder = new URIBuilder(url);
		for (Map.Entry<String, Object> entry : param.entrySet()) {
			uriBuilder
					.addParameter(entry.getKey(), entry.getValue().toString());
		}
		return doGet(uriBuilder.build().toString());
	}

	/**
	 *
	 * @描述：指定POST请求
	 * @创建人：wyait
	 * @创建时间：2015年10月13日 下午2:57:31
	 * @param url 请求地址
	 * @param param 请求参数
	 * @return 状态码和请求的body
	 * @throws IOException
	 */
	public HttpResult doPost(String url, Map<String, Object> param)
			throws IOException {
		// 创建http POST请求
		HttpPost httpPost = new HttpPost(url);
		httpPost.setConfig(this.requestConfig);
		if (param != null) {
			// 设置post参数
			List<NameValuePair> parameters = new ArrayList<NameValuePair>(0);
			for (Map.Entry<String, Object> entry : param.entrySet()) {
				parameters.add(new BasicNameValuePair(entry.getKey(), entry
						.getValue() + ""));
			}
			// 构造一个form表单式的实体,并且指定参数的编码为UTF-8
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
					parameters, "UTF-8");
			// 将请求实体设置到httpPost对象中
			httpPost.setEntity(formEntity);
		}
		CloseableHttpResponse response = null;
		try {
			// 执行请求
			response = httpClient.execute(httpPost);
			if (response.getEntity() != null) {
				return new HttpResult(response.getStatusLine().getStatusCode(),
						EntityUtils.toString(response.getEntity(), "UTF-8"));
			}
			return new HttpResult(response.getStatusLine().getStatusCode(),
					null);
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}

	/**
	 *
	 * @描述：指定POST请求
	 * @创建人：wyait
	 * @创建时间：2015年10月13日 下午2:57:31
	 * @param url 请求地址
	 * @param param 请求参数
	 * @return 状态码和请求的body
	 * @throws IOException
	 */
	public HttpResult doPostPic(String url, Map<String, Object> param)
			throws IOException {
		// 创建http POST请求
		HttpPost httpPost = new HttpPost(url);
		httpPost.setConfig(this.requestConfig);
		httpPost.addHeader("Content-Type", "multipart/form-data");
		if (param != null) {
			// 设置post参数
			List<NameValuePair> parameters = new ArrayList<NameValuePair>(0);
			for (Map.Entry<String, Object> entry : param.entrySet()) {
				parameters.add(new BasicNameValuePair(entry.getKey(), entry
						.getValue() + ""));
			}
			// 构造一个form表单式的实体,并且指定参数的编码为UTF-8
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
					parameters, "UTF-8");
			// 将请求实体设置到httpPost对象中
			httpPost.setEntity(formEntity);
		}
		CloseableHttpResponse response = null;
		try {
			// 执行请求
			response = httpClient.execute(httpPost);
			if (response.getEntity() != null) {
				return new HttpResult(response.getStatusLine().getStatusCode(),
						EntityUtils.toString(response.getEntity(), "UTF-8"));
			}
			return new HttpResult(response.getStatusLine().getStatusCode(),
					null);
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}

	/**
	 *
	 * @描述：执行PUT请求
	 * @创建人：wyait
	 * @创建时间：2015年10月13日 下午2:58:08
	 * @param url 请求地址
	 * @param param 请求参数
	 * @return 状态码和请求的body
	 * @throws IOException
	 */
	public HttpResult doPut(String url, Map<String, Object> param)
			throws IOException {
		// 创建http POST请求
		HttpPut httpPut = new HttpPut(url);
		httpPut.setConfig(this.requestConfig);
		if (param != null) {
			// 设置post参数
			List<NameValuePair> parameters = new ArrayList<NameValuePair>(0);
			for (Map.Entry<String, Object> entry : param.entrySet()) {
				parameters.add(new BasicNameValuePair(entry.getKey(), entry
						.getValue().toString()));
			}
			// 构造一个form表单式的实体,并且指定参数的编码为UTF-8
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
					parameters, "UTF-8");
			// 将请求实体设置到httpPost对象中
			httpPut.setEntity(formEntity);
		}

		CloseableHttpResponse response = null;
		try {
			// 执行请求
			response = httpClient.execute(httpPut);
			if (response.getEntity() != null) {
				return new HttpResult(response.getStatusLine().getStatusCode(),
						EntityUtils.toString(response.getEntity(), "UTF-8"));
			}
			return new HttpResult(response.getStatusLine().getStatusCode(),
					null);
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}

	/**
	 *
	 * @描述：指定POST请求
	 * @创建人：wyait
	 * @创建时间：2015年10月13日 下午2:58:46
	 * @param url 请求地址
	 * @param json 请求参数
	 * @return
	 * @throws IOException
	 */
	public HttpResult doPostJson(String url, String json) throws IOException {
		// 创建http POST请求
		HttpPost httpPost = new HttpPost(url);
		httpPost.setConfig(this.requestConfig);
		if (json != null) {
			// 构造一个字符串的实体
			StringEntity stringEntity = new StringEntity(json,
					ContentType.APPLICATION_JSON);
			// 将请求实体设置到httpPost对象中
			httpPost.setEntity(stringEntity);
		}

		CloseableHttpResponse response = null;
		try {
			// 执行请求
			response = httpClient.execute(httpPost);
			if (response.getEntity() != null) {
				return new HttpResult(response.getStatusLine().getStatusCode(),
						EntityUtils.toString(response.getEntity(), "UTF-8"));
			}
			return new HttpResult(response.getStatusLine().getStatusCode(),
					null);
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}

	/**
	 *
	 * @描述：没有参数的post请求
	 * @创建人：wyait
	 * @创建时间：2015年10月13日 下午2:59:26
	 * @param url 请求地址
	 * @return
	 * @throws IOException
	 */
	public HttpResult doPost(String url) throws IOException {
		return doPost(url, null);
	}

	/**
	 *
	 * @描述：执行PUT请求
	 * @创建人：wyait
	 * @创建时间：2015年10月13日 下午2:59:46
	 * @param url 请求地址
	 * @return 状态码和请求的body
	 * @throws IOException
	 */
	public HttpResult doPut(String url) throws IOException {
		return this.doPut(url, null);
	}

	/**
	 *
	 * @描述：执行DELETE请求,通过POST提交，_method指定真正的请求方法
	 * @创建人：wyait
	 * @创建时间：2015年10月13日 下午3:00:10
	 * @param url 请求地址
	 * @param param 请求参数
	 * @return 状态码和请求的body
	 * @throws IOException
	 */
	public HttpResult doDelete(String url, Map<String, Object> param)
			throws IOException {
		param.put("_method", "DELETE");
		return this.doPost(url, param);
	}

	/**
	 *
	 * @描述：执行DELETE请求(真正的DELETE请求)
	 * @创建人：wyait
	 * @创建时间：2015年10月13日 下午3:00:36
	 * @param url 请求地址
	 * @return 状态码和请求的body
	 * @throws IOException
	 */
	public HttpResult doDelete(String url) throws IOException {
		// 创建http DELETE请求
		HttpDelete httpDelete = new HttpDelete(url);
		httpDelete.setConfig(this.requestConfig);
		CloseableHttpResponse response = null;
		try {
			// 执行请求
			response = httpClient.execute(httpDelete);
			if (response.getEntity() != null) {
				return new HttpResult(response.getStatusLine().getStatusCode(),
						EntityUtils.toString(response.getEntity(), "UTF-8"));
			}
			return new HttpResult(response.getStatusLine().getStatusCode(),
					null);
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}

	/**
	 *
	 * @描述：httpCilent多图片上传和多个参数
	 * @创建人：wyait
	 * @创建时间：2017年5月2日 下午1:47:41
	 * @param url
	 * @param params
	 * @param files file对象必须包含图片地址
	 * @return
	 * @throws IOException
	 */
	public HttpResult postUploadFile(String url, Map<String, Object> params,
			Map<String, File> files) throws IOException {
		HttpPost httpPost = new HttpPost(url);// 创建 HTTP POST 请求
		httpPost.setConfig(this.requestConfig);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setCharset(Charset.forName("UTF-8"));// 设置请求的编码格式
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);// 设置浏览器兼容模式
		// 设置参数
		if (files != null) {
			// 设置图片参数
			for (Map.Entry<String, File> entry : files.entrySet()) {
				builder.addBinaryBody(entry.getKey(), entry.getValue());
			}
		}
		// 设置参数
		if (params != null) {
			// 设置post参数
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				// 指定编码，防止中文乱码问题。但是某些情况下会导致上传失败
				builder.addTextBody(entry.getKey(),
						String.valueOf(entry.getValue()),
						ContentType.create("text/plain", "UTF-8"));
			}
		}
		// 生成 HTTP POST 实体
		HttpEntity entity = builder.build();
		/**
		 * 有些网站后台使用的编码和页面源码上写的编码不一致
		 * 或者页面上的编码，和后台服务的编码不一致。页面上都是gbk的，服务器端都是utf-8的，就会导致上传失败；
		 * 解决办法：强制去除contentType中的编码设置
		 */
		// 强制去除contentType中的编码设置，否则，在某些情况下会导致上传失败
		// boolean forceRemoveContentTypeCharset = (Boolean)
		// params.get(".rmCharset");
		// if (forceRemoveContentTypeCharset) {
		// removeContentTypeChraset("UTF-8", entity);
		// }
		// 设置请求参数
		httpPost.setEntity(entity);
		CloseableHttpResponse response = null;
		try {
			// 执行请求
			response = httpClient.execute(httpPost);
			if (response.getEntity() != null) {
				return new HttpResult(response.getStatusLine().getStatusCode(),
						EntityUtils.toString(response.getEntity(), "UTF-8"));
			}
			return new HttpResult(response.getStatusLine().getStatusCode(),
					null);
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}

	/**
	 * @param url servlet的地址 
	 * @param params 要传递的参数 
	 * @param files 要上传的文件 
	 * @return true if upload success else false 
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	// @SuppressWarnings("all")
	// private boolean uploadFiles(String url, Map<String, String> params,
	// ArrayList<File> files) throws ClientProtocolException, IOException {
	// HttpClient client = new DefaultHttpClient();// 开启一个客户端 HTTP 请求
	// HttpPost post = new HttpPost(url);// 创建 HTTP POST 请求
	// MultipartEntityBuilder builder = MultipartEntityBuilder.create();
	// // builder.setCharset(Charset.forName("uft-8"));//设置请求的编码格式
	// builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);// 设置浏览器兼容模式
	// int count = 0;
	// for (File file : files) {
	// // FileBody fileBody = new FileBody(file);//把文件转换成流对象FileBody
	// // builder.addPart("file"+count, fileBody);
	// builder.addBinaryBody("file" + count, file);
	// count++;
	// }
	// builder.addTextBody("method", params.get("method"));// 设置请求参数
	// builder.addTextBody("fileTypes", params.get("fileTypes"));// 设置请求参数
	// HttpEntity entity = builder.build();// 生成 HTTP POST 实体
	// post.setEntity(entity);// 设置请求参数
	// HttpResponse response = client.execute(post);// 发起请求 并返回请求的响应
	// if (response.getStatusLine().getStatusCode() == 200) {
	// return true;
	// }
	// return false;
	// }
	/**
	 *
	 * @描述：
	 * 这里面有一个方法removeContentTypeChraset，主要是为了解决，如果调用了setCharset，
	 * </br>中文文件名不会乱码，但是在ContentType文件头中会多一个charset=xxx，而导致上传失败，
	 * </br>解决办法就是强制去掉这个信息。而这个HttpEntity实际对象是MultipartFormEntity对象。
	 * </br>这个类未声明public，所以只能包内访问。而且该类的contentType属性是private final类型。
	 * </br>就算可以通过对象拿到这个属性，也无法修改。所以我只能通过反射来修改。
	 * @创建人：wyait
	 * @创建时间：2017年5月3日 下午3:53:49
	 * @param encoding
	 * @param entity
	 */
	@SuppressWarnings("unused")
	private static void removeContentTypeChraset(String encoding,
			HttpEntity entity) {
		try {
			Class<?> clazz = entity.getClass();
			Field field = clazz.getDeclaredField("contentType");
			field.setAccessible(true); // 将字段的访问权限设为true：即去除private修饰符的影响
			if (Modifier.isFinal(field.getModifiers())) {
				Field modifiersField = Field.class
						.getDeclaredField("modifiers"); // 去除final修饰符的影响，将字段设为可修改的
				modifiersField.setAccessible(true);
				modifiersField.setInt(field, field.getModifiers()
						& ~Modifier.FINAL);
			}
			BasicHeader o = (BasicHeader) field.get(entity);
			field.set(entity, new BasicHeader(HTTP.CONTENT_TYPE, o.getValue()
					.replace("; charset=" + encoding, "")));
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
