package com.cws.shop.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.cws.shop.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {


    private final JavaMailSender mailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendResetPasswordEmail(String to, String subject, String body) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(body);
        msg.setFrom("pratik.cws1@gmail.com");
        mailSender.send(msg);
    }

	@Override
	public void sendVerificationEmail(String email, String token) {
		
		String link = "http://localhost:8080/api/verify?token=" + token;
		
		SimpleMailMessage message = new SimpleMailMessage();
		
		message.setTo(email);
		message.setSubject("Please Verify Your Account");
		message.setFrom("pratik.cws1@gmail.com");
		message.setText("Click on the link to verify your account: http://localhost:8080/api/verify?token=" + token);
		
		mailSender.send(message);
	}
}