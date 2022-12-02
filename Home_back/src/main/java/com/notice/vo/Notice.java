package com.notice.vo;

import io.swagger.annotations.ApiModelProperty;

public class Notice {

	@ApiModelProperty(value = "공지사항 num")
	private String num;
	
	@ApiModelProperty(value = "제목")
	private String title;
	
	@ApiModelProperty(value = "내용")
	private String content;
	
	@ApiModelProperty(value = "등록 날짜")
	private String regdate;

	@ApiModelProperty(value = "조회수")
	private String count;

	public Notice(){}
	public Notice(String num, String title, String content, String regdate, String count) {
		super();
		this.num = num;
		this.title = title;
		this.content = content;
		this.regdate = regdate;
		this.count = count;
	}
	
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getRegdate() {
		return regdate;
	}
	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	
}
