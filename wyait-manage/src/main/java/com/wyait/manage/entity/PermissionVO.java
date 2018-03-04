package com.wyait.manage.entity;

import java.io.Serializable;

public class PermissionVO implements Serializable{
	private static final long serialVersionUID = -2783081162690878303L;
	private String id;

	private String name;

	private String pId;

	private String istype;

	private String code;

	private String page;

	private String icon;

	private String zindex;

	private boolean checked;

	private boolean open;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getIstype() {
		return istype;
	}

	public void setIstype(String istype) {
		this.istype = istype;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getZindex() {
		return zindex;
	}

	public void setZindex(String zindex) {
		this.zindex = zindex;
	}

	public boolean getChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public boolean getOpen() {
		return true;
	}

	/*public void setOpen(boolean open) {
		this.open = open;
	}*/

	@Override public String toString() {
		return "PermissionVO{" + "id='" + id + '\'' + ", name='" + name + '\''
				+ ", pId='" + pId + '\'' + ", istype='" + istype + '\''
				+ ", code='" + code + '\'' + ", page='" + page + '\''
				+ ", icon='" + icon + '\'' + ", zindex='" + zindex + '\''
				+ ", checked=" + checked + ", open=" + open + '}';
	}
}