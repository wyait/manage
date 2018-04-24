package com.wyait.manage.entity;

/**
 * @项目名称：wyait-manage
 * @包名：com.wyait.manage.entity
 * @类描述：
 * @创建人：wyait
 * @创建时间：2018-01-02 17:10
 * @version：V1.0
 */
public class UserSearchDTO {

	private Integer page;

	private Integer limit;

	private String uname;

	private String umobile;

	private String insertTimeStart;

	private String insertTimeEnd;

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getUmobile() {
		return umobile;
	}

	public void setUmobile(String umobile) {
		this.umobile = umobile;
	}

	public String getInsertTimeStart() {
		return insertTimeStart;
	}

	public void setInsertTimeStart(String insertTimeStart) {
		this.insertTimeStart = insertTimeStart;
	}

	public String getInsertTimeEnd() {
		return insertTimeEnd;
	}

	public void setInsertTimeEnd(String insertTimeEnd) {
		this.insertTimeEnd = insertTimeEnd;
	}

	@Override public String toString() {
		return "UserSearchDTO{" + "page=" + page + ", limit=" + limit
				+ ", uname='" + uname + '\'' + ", umobile='" + umobile + '\''
				+ ", insertTimeStart='" + insertTimeStart + '\''
				+ ", insertTimeEnd='" + insertTimeEnd + '\'' + '}';
	}
}
