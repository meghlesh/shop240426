package com.cws.shop.service;

public interface EmailService {
	void sendVerificationEmail(String email, String token);
	
    void sendResetPasswordEmail(String to, String subject, String body);
}
