package com.favorite.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class Favorite {

	@ApiModelProperty(value = "관심지역 번호")
	private String num;
	@ApiModelProperty(value = "회원 아이디")
	private String id;
	@ApiModelProperty(value = "관심지역 동코드")
	private String dongCode;
	@ApiModelProperty(value = "관심지역 시도명")
	private String sidoName;
	@ApiModelProperty(value = "관심지역 구군명")
	private String gugunName;
	@ApiModelProperty(value = "관심지역 읍면동 명")
	private String dongName;

	public Favorite() {
	}

	public Favorite(String num, String id, String dongCode, String sidoName, String gugunName, String dongName) {
		this.num = num;
		this.id = id;
		this.dongCode = dongCode;
		this.sidoName = sidoName;
		this.gugunName = gugunName;
		this.dongName = dongName;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDongCode() {
		return dongCode;
	}

	public void setDongCode(String dongCode) {
		this.dongCode = dongCode;
	}

	public String getSidoName() {
		return sidoName;
	}

	public void setSidoName(String sidoName) {
		this.sidoName = sidoName;
	}

	public String getGugunName() {
		return gugunName;
	}

	public void setGugunName(String gugunName) {
		this.gugunName = gugunName;
	}

	public String getDongName() {
		return dongName;
	}

	public void setDongName(String dongName) {
		this.dongName = dongName;
	}

}
