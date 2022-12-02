package com.home.vo;


public class AddressFilter {
	String dongCode;
	String dealYear;
	String dealMonth;
	int minPrice;
	int maxPrice;
	double minArea;
	double maxArea;
	int minFloor;
	int maxFloor;
	int minYear;
	int maxYear;

	public AddressFilter() {

	}

	public AddressFilter(String dongCode, String dealYear, String dealMonth, int minPrice, int maxPrice, double minArea,
			double maxArea, int minFloor, int maxFloor, int minYear, int maxYear) {
		super();
		this.dongCode = dongCode;
		this.dealYear = dealYear;
		this.dealMonth = dealMonth;
		this.minPrice = minPrice;
		this.maxPrice = maxPrice;
		this.minArea = minArea;
		this.maxArea = maxArea;
		this.minFloor = minFloor;
		this.maxFloor = maxFloor;
		this.minYear = minYear;
		this.maxYear = maxYear;
	}

	public String getDongCode() {
		return dongCode;
	}

	public void setDongCode(String dongCode) {
		this.dongCode = dongCode;
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

	public int getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(int minPrice) {
		this.minPrice = minPrice;
	}

	public int getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(int maxPrice) {
		this.maxPrice = maxPrice;
	}

	public double getMinArea() {
		return minArea;
	}

	public void setMinArea(double minArea) {
		this.minArea = minArea;
	}

	public double getMaxArea() {
		return maxArea;
	}

	public void setMaxArea(double maxArea) {
		this.maxArea = maxArea;
	}

	public int getMinFloor() {
		return minFloor;
	}

	public void setMinFloor(int minFloor) {
		this.minFloor = minFloor;
	}

	public int getMaxFloor() {
		return maxFloor;
	}

	public void setMaxFloor(int maxFloor) {
		this.maxFloor = maxFloor;
	}

	public int getMinYear() {
		return minYear;
	}

	public void setMinYear(int minYear) {
		this.minYear = minYear;
	}

	public int getMaxYear() {
		return maxYear;
	}

	public void setMaxYear(int maxYear) {
		this.maxYear = maxYear;
	}

}