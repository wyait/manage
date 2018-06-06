package com.wyait.manage.utils;
/**
 * 
 * @项目名称：wyait-manage
 * @类名称：ExceptionEnum
 * @类描述：自定义异常枚举类
 * @创建人：wyait
 * @创建时间：2018年5月30日 下午2:33:08 
 * @version：
 */
public enum ExceptionEnum {
    UNKNOW_ERROR(-1,"UnknowError","未知错误"),
    USER_NOT_FIND(-101,"UserNotFind","用户不存在"),
    BAD_REQUEST(400,"BadRequest","请求有误"),
    FORBIDDEN(403,"Forbidden","权限不足"),
    NOT_FOUND(404,"NotFound","您所访问的资源不存在"),
    SERVER_EPT(500,"ServerEpt","操作异常，请稍后再试");

    private Integer type;
    private String code;
    private String msg;

    ExceptionEnum(Integer type,String code, String msg) {
        this.type = type;
        this.code = code;
        this.msg = msg;
    }

    public Integer getType() {
        return type;
    }
    public String getCode() {
    	return code;
    }

    public String getMsg() {
        return msg;
    }
    
}