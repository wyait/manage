package com.wyait.manage.entity;

import net.sf.oval.constraint.MatchPattern;
import net.sf.oval.constraint.NotNull;
import net.sf.oval.constraint.ValidateWithMethod;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class UserDTO {

	private Integer id;
	@NotNull(message = "用户名不能为空，请您先填写用户名")
	private String username;
	@NotNull(message = "手机号不能为空，请您先填写手机号")
	private String mobile;

	private String email;
	@NotNull(message = "密码不能为空，请您先填写手机号")
	@MatchPattern(pattern = "^[0-9_a-zA-Z]{6,20}$", message = "用户名或密码有误，请您重新填写")
	private String password;
	@NotNull(message = "图片验证码不能为空，请您先填写验证码")
	@MatchPattern(pattern = "\\w{4}$", message = "图片验证码有误，请您重新填写")
	private String code;

	@ValidateWithMethod(methodName = "isValidateSmsCode", message = "验证码有误，请您重新填写", parameterType = String.class)
	private String smsCode;

	private static final Pattern CODE = Pattern.compile("[0-9]{6}$");

	/**
	 *	是否为6位短信验证码
	 * @param smsCode
	 * @return
	 */
	private boolean isValidateSmsCode(String smsCode){
		if(StringUtils.isNotBlank(smsCode)){
			if (!CODE.matcher(smsCode).matches()) {
				return false;
			}
		}
		return true;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

	@Override public String toString() {
		return "UserDTO{" + "id=" + id + ", username='" + username + '\''
				+ ", mobile='" + mobile + '\'' + ", email='" + email + '\''
				+ ", password='" + password + '\'' + ", code='" + code + '\''
				+ ", smsCode='" + smsCode + '\'' + '}';
	}
}