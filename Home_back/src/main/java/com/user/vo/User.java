package com.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class User {
	@ApiModelProperty(value = "회원 아이디")
	private String id;
	@ApiModelProperty(value = "회원 비밀번호")
	private String pass;
	@ApiModelProperty(value = "회원 이름")
	private String name;
	@ApiModelProperty(value = "회원 전화번호")
	private String phone;
	@ApiModelProperty(value = "회원 이메일")
	private String email;

	public User() {
	}

	public User(String id, String pass, String name, String phone, String email) {
		super();
		this.id = id;
		this.pass = pass;
		this.name = name;
		this.phone = phone;
		this.email = email;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
