package com.wyait.manage.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @项目名称：common
 * @类名称：HttpResult
 * @类描述：客户端：封装接收到的http请求返回结果
 * @创建人：wyait 
 * @创建时间：2015年10月13日 下午2:53:09 
 * @version：1.0.0
 */
public class HttpResult {

	// 响应状态码
	private Integer code;

	// 响应体
	private String body;

	public HttpResult() {

	}

	public HttpResult(Integer code, String body) {
		super();
		this.code = code;
		if (StringUtils.isNotEmpty(body)) {
			this.body = body;
		}

	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "HttpResult [code=" + code + ", body=" + body + "]";
	}

}
