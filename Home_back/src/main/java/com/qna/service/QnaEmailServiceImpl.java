package com.qna.service;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.user.service.UserService;

@Service
public class QnaEmailServiceImpl implements QnaEmailService {

	@Autowired
	UserService userService;

	@Autowired
	private JavaMailSender mailSender;

	@Value("${spring.mail.username}")
	private String from;

	@Override
	public MimeMessage sendMail(String userEmail, String userId) throws Exception {
		String title = userId + "님 WhereIsMyHome에 문의하신 내용에 답변 완료되었습니다.";
		StringBuilder content = new StringBuilder();
		content.append("안녕하세요. ").append(userId).append("님,").append("<br><br>").append("문의하신 내용에 답변이 완료되었습니다.")
				.append("<br><br>").append("WhereIsMyHome 나의 문의 게시판에서 확인해주세요!!<br>").append("<a href='http://localhost'>보러가기</a>");
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

}
