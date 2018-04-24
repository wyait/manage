package com.wyait.manage.entity;

import com.wyait.manage.pojo.RolePermissionKey;

import java.util.List;

/**
 * @项目名称：wyait-manage
 * @包名：com.wyait.manage.entity
 * @类描述：
 * @创建人：wyait
 * @创建时间：2017-12-28 19:57
 * @version：V1.0
 */
public class RoleVO {

	private Integer id;

	private String roleName;

	private String descpt;

	private String code;

	private Integer insertUid;

	private String insertTime;
	//角色下的权限ids
	private List<RolePermissionKey> rolePerms;

	public Integer getId() {
		return id;
	}

	public String getRoleName() {
		return roleName;
	}

	public String getDescpt() {
		return descpt;
	}

	public String getCode() {
		return code;
	}

	public Integer getInsertUid() {
		return insertUid;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public void setDescpt(String descpt) {
		this.descpt = descpt;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setInsertUid(Integer insertUid) {
		this.insertUid = insertUid;
	}

	public List<RolePermissionKey> getRolePerms() {
		return rolePerms;
	}

	public void setRolePerms(List<RolePermissionKey> rolePerms) {
		this.rolePerms = rolePerms;
	}

	public String getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(String insertTime) {
		this.insertTime = insertTime;
	}

	@Override public String toString() {
		return "RoleVO{" + "id=" + id + ", roleName='" + roleName + '\''
				+ ", descpt='" + descpt + '\'' + ", code='" + code + '\''
				+ ", insertUid=" + insertUid + ", insertTime=" + insertTime
				+ ", rolePerms=" + rolePerms + '}';
	}
}
