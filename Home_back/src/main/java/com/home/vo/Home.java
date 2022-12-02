package com.home.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class Home {
	
	@ApiModelProperty(value = "프라이머리")
	private String num;

	// 법정동시군구코드+법정동읍면동코드 → DB dongcode에서 시군구 정보 가져오기
	@ApiModelProperty(value = "동코드")
	private String dongCode;

	@ApiModelProperty(value = "아파트 이름")
	private String apartmentName;

	@ApiModelProperty(value = "도로명")
	private String roadName;

	@ApiModelProperty(value = "도로명 건물 본번호코드")
	private String roadNameBuildingBonCode;

	@ApiModelProperty(value = "도로명 건물 부번호코드")
	private String roadNameBuildingBuCode;

	@ApiModelProperty(value = "매매 가격")
	private String dealAmount;

	@ApiModelProperty(value = "매매 년도")
	private String dealYear;

	@ApiModelProperty(value = "매매 월")
	private String dealMonth;

	@ApiModelProperty(value = "매매 일")
	private String dealDay;

	@ApiModelProperty(value = "면적")
	private String area;

	@ApiModelProperty(value = "층")
	private String floor;

	@ApiModelProperty(value = "건축년도")
	private String buildYear;

	private int count;

	public Home() {
	}

	public Home(String num, String dongCode, String apartmentName, String roadName, String roadNameBuildingBonCode,
			String roadNameBuildingBuCode, String dealAmount, String dealYear, String dealMonth, String dealDay,
			String area, String floor, String buildYear, int count) {
		super();
		this.num = num;
		this.dongCode = dongCode;
		this.apartmentName = apartmentName;
		this.roadName = roadName;
		this.roadNameBuildingBonCode = roadNameBuildingBonCode;
		this.roadNameBuildingBuCode = roadNameBuildingBuCode;
		this.dealAmount = dealAmount;
		this.dealYear = dealYear;
		this.dealMonth = dealMonth;
		this.dealDay = dealDay;
		this.area = area;
		this.floor = floor;
		this.buildYear = buildYear;
		this.count = count;
	}

	public Home(String dongCode, String apartmentName, String roadName, String roadNameBuildingBonCode,
			String roadNameBuildingBuCode, String dealAmount, String dealYear, String dealMonth, String dealDay,
			String area, String floor, String buildYear) {
		super();
		this.dongCode = dongCode;
		this.apartmentName = apartmentName;
		this.roadName = roadName;
		this.roadNameBuildingBonCode = roadNameBuildingBonCode;
		this.roadNameBuildingBuCode = roadNameBuildingBuCode;
		this.dealAmount = dealAmount;
		this.dealYear = dealYear;
		this.dealMonth = dealMonth;
		this.dealDay = dealDay;
		this.area = area;
		this.floor = floor;
		this.buildYear = buildYear;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getDongCode() {
		return dongCode;
	}

	public void setDongCode(String dongCode) {
		this.dongCode = dongCode;
	}

	public String getApartmentName() {
		return apartmentName;
	}

	public void setApartmentName(String apartmentName) {
		this.apartmentName = apartmentName;
	}

	public String getRoadName() {
		return roadName;
	}

	public void setRoadName(String roadName) {
		this.roadName = roadName;
	}

	public String getRoadNameBuildingBonCode() {
		return roadNameBuildingBonCode;
	}

	public void setRoadNameBuildingBonCode(String roadNameBuildingBonCode) {
		this.roadNameBuildingBonCode = roadNameBuildingBonCode;
	}

	public String getRoadNameBuildingBuCode() {
		return roadNameBuildingBuCode;
	}

	public void setRoadNameBuildingBuCode(String roadNameBuildingBuCode) {
		this.roadNameBuildingBuCode = roadNameBuildingBuCode;
	}

	public String getDealAmount() {
		return dealAmount;
	}

	public void setDealAmount(String dealAmount) {
		this.dealAmount = dealAmount;
	}

	public String getDealYear() {
		return dealYear;
	}

	public void setDealYear(String dealYear) {
		this.dealYear = dealYear;
	}

	public String getDealMonth() {
		return dealMonth;
	}

	public void setDealMonth(String dealMonth) {
		this.dealMonth = dealMonth;
	}

	public String getDealDay() {
		return dealDay;
	}

	public void setDealDay(String dealDay) {
		this.dealDay = dealDay;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String getBuildYear() {
		return buildYear;
	}

	public void setBuildYear(String buildYear) {
		this.buildYear = buildYear;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
