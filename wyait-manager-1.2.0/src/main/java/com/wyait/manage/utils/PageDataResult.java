package com.wyait.manage.utils;

import java.util.List;

/**
 *
 * @项目名称：wyait-manage
 * @类名称：PageDateResult
 * @类描述：封装DTO分页数据（记录数和所有记录）
 * @创建人：wyait
 * @创建时间：2017年12月31日14:49:34
 * @version：2.0.0
 */
public class PageDataResult {

	//总记录数量
	private Integer totals;
	//当前页数据列表
	private List<?> list;

	private Integer code=200;

	public PageDataResult() {
	}

	public PageDataResult( Integer totals,
			List<?> list) {
		this.totals = totals;
		this.list = list;
	}

	public Integer getTotals() {
		return totals;
	}

	public void setTotals(Integer totals) {
		this.totals = totals;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	@Override public String toString() {
		return "PageDataResult{" + "totals=" + totals + ", list=" + list
				+ ", code=" + code + '}';
	}
}
