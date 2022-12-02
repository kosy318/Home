package com.qna.vo;

import io.swagger.annotations.ApiModelProperty;

public class Qna {

	@Override
	public String toString() {
		return "Qna [num=" + num + ", secret=" + secret + ", id=" + id + ", title=" + title + ", content=" + content
				+ ", answer=" + answer + ", regdate=" + regdate + ", count=" + count + ", checked=" + checked
				+ ", email=" + email + "]";
	}

	@ApiModelProperty(value = "qna num")
	private String num;

	@ApiModelProperty(value = "비공개")
	private String secret;

	@ApiModelProperty(value = "id")
	private String id;

	@ApiModelProperty(value = "제목")
	private String title;

	@ApiModelProperty(value = "내용")
	private String content;

	@ApiModelProperty(value = "답")
	private String answer;

	@ApiModelProperty(value = "등록 날짜")
	private String regdate;

	@ApiModelProperty(value = "조회수")
	private String count;

	@ApiModelProperty(value = "확인 여부")
	private String checked;

	@ApiModelProperty(value = "이메일 전송 여부")
	private String email;

	public Qna() {
	}

	public Qna(String num, String secret, String id, String title, String content, String answer, String regdate,
			String count, String checked, String email) {
		super();
		this.num = num;
		this.secret = secret;
		this.id = id;
		this.title = title;
		this.content = content;
		this.answer = answer;
		this.regdate = regdate;
		this.count = count;
		this.checked = checked;
		this.email = email;
	}

	public String getChecked() {
		return checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
