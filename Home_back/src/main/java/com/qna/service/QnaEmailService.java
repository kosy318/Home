package com.qna.service;


import javax.mail.internet.MimeMessage;

public interface QnaEmailService {
	
	public MimeMessage sendMail(String userEmail, String userId) throws Exception;

}
