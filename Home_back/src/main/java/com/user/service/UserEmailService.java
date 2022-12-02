package com.user.service;


import javax.mail.internet.MimeMessage;

public interface UserEmailService {
	
	public MimeMessage sendMailAndChangePassword(String userEmail, String userId) throws Exception;
	
	public String sendMailAndValidCode(String email) throws Exception;

	public void updatePassword(String str, String userEmail) throws Exception;

	public String getTempPassword() throws Exception;

}
