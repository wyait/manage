package com.wyait.manage.pojo;

public class UserRoleKey {
    private Integer userId;

    private Integer roleId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    @Override public String toString() {
        return "UserRoleKey{" + "userId=" + userId + ", roleId=" + roleId + '}';
    }
}