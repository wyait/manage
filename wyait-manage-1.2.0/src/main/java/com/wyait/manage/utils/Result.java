package com.wyait.manage.utils;

/**
 * 
 * @项目名称：wyait-manage
 * @类名称：Result
 * @类描述：http请求返回的对象
 * @创建人：wyait
 * @创建时间：2018年6月5日 下午4:40:52 
 * @version：
 */
public class Result<T> {

	private String status;// 响应状态编码
	private String message;// 响应信息
	private T data;// 返回成功信息

	private Result() {
		// 单例
	}

	private static final Result ME = new Result();

	public static Result getInstance() {
		return ME;
	}

	/**
	 * 响应status和message
	 * @param status
	 * @param message
	 */
	public Result(String status, String message) {
		this.status = status;
		this.message = message;
	}

	/**
	 * 响应status、message和result
	 * @param status
	 * @param message
	 * @param data
	 */
	public Result(String status, String message, T data) {
		this.status = status;
		this.message = message;
		this.data = data;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Result [status=" + status + ", message=" + message + ", data="
				+ data + "]";
	}

}