package com.user.service;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class UserEmailServiceImpl implements UserEmailService {

	@Autowired
	UserService userService;

	@Autowired
	private JavaMailSender mailSender;

	@Value("${spring.mail.username}")
	private String from;

	@Override
	public MimeMessage sendMailAndChangePassword(String userEmail, String userId) throws Exception {
		String tempPass = getTempPassword();
		updatePassword(tempPass, userId);
		String title = userId + "님 WhereIsMyHome에서 임시 비밀번호를 보내드립니다.";
		StringBuilder content = new StringBuilder();
		content.append("안녕하세요. ").append(userId).append("님,").append("<br><br>").append("WhereIsMyHome에서 임시 비밀번호를 보내드립니다.")
				.append("<br>").append(userId).append("님의 임시비밀번호는 <span style='font-weight:bold; color:blue'>").append(tempPass).append("</span>입니다.").append("<br><br>")
				.append("WhereIsMyHome으로 돌아가 입력해주세요!!<br>").append("<a href='http://localhost/login'>로그인하러 가기</a>");
		// html형식으로 메일 보내기 -> 링크가능
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, "utf-8");
		mimeMessageHelper.setFrom(from);
		mimeMessageHelper.setTo(userEmail);
		mimeMessageHelper.setSubject(title);
		mimeMessageHelper.setText(content.toString(), true);
		mailSender.send(message);

		return message;
	}
	
	@Override
	public String sendMailAndValidCode(String email) throws Exception {
		String code = getTempPassword();
		
		String title = "WhereIsMyHome에서 이메일 인증 코드 보내드립니다.";
		StringBuilder content = new StringBuilder();
		content.append("안녕하세요.").append("<br><br>").append("WhereIsMyHome에서 이메일 인증 코드 보내드립니다.")
				.append("<br>").append("인증코드는 ").append(code).append("입니다.").append("<br><br>")
				.append("WhereIsMyHome으로 돌아가 입력해주세요!!<br>");
		// html형식으로 메일 보내기 -> 링크가능
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, "utf-8");
		mimeMessageHelper.setFrom(from);
		mimeMessageHelper.setTo(email);
		mimeMessageHelper.setSubject(title);
		mimeMessageHelper.setText(content.toString(), true);
		System.out.println(message.toString());
		mailSender.send(message);

		return code;
	}

	@Override
	public void updatePassword(String tempPassword, String userId) throws Exception {
		userService.updateUserPass(userId, tempPassword);
	}

	@Override
	public String getTempPassword() {
		char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
				'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

		String tempPassword = "";

		int idx = 0;
		for (int i = 0; i < 10; i++) {
			idx = (int) (charSet.length * Math.random());
			tempPassword += charSet[idx];
		}
		return tempPassword;
	}

}
