package com.cws.shop.service;

import com.cws.shop.dto.request.ForgotPasswordRequestDto;
import com.cws.shop.dto.request.LoginRequestDto;
import com.cws.shop.dto.request.RegisterRequestDto;
import com.cws.shop.dto.request.ResetPasswordDto;
import com.cws.shop.dto.response.ApiResponse;
import com.cws.shop.model.User;

public interface AuthService {
	ApiResponse<?> register(RegisterRequestDto request);
    
	ApiResponse<?> login(LoginRequestDto request);

	ApiResponse<?> forgotPassword(ForgotPasswordRequestDto request);

	ApiResponse<?> resetPassword(ResetPasswordDto request);
	
	User verifyEmail(String token);
	
	void validateResetToken(String token);
	
	ApiResponse<?> resendVerificationEmail(String email);
}