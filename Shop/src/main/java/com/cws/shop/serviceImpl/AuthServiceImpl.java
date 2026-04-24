package com.cws.shop.serviceImpl;

import com.cws.shop.dto.request.ForgotPasswordRequestDto;
import com.cws.shop.dto.request.LoginRequestDto;
import com.cws.shop.dto.request.RegisterRequestDto;
import com.cws.shop.dto.request.ResetPasswordDto;
import com.cws.shop.dto.response.ApiResponse;
import com.cws.shop.dto.response.AuthResponseDto;
import com.cws.shop.exception.*;
import com.cws.shop.security.JwtUtil;
import com.cws.shop.service.EmailService;
import com.cws.shop.service.NotificationService;
import com.cws.shop.service.TokenService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cws.shop.model.NotificationType;
import com.cws.shop.model.Role;
import com.cws.shop.model.Token;
import com.cws.shop.model.TokenType;
import com.cws.shop.model.User;
import com.cws.shop.repository.UserRepository;
import com.cws.shop.service.AuthService;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Transactional
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private TokenService tokenService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final NotificationService notificationService;
	
    
    private static final long EMAIL_VERIFICATION_HOURS = 24;
    private static final long PASSWORD_RESET_HOURS = 1;
    
    public AuthServiceImpl(UserRepository userRepository,TokenService tokenService, EmailService emailService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtil, NotificationService notificationService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.notificationService = notificationService;
    }

    @Override
    public ApiResponse<?> register(RegisterRequestDto request) {

        // Check if email already exists
        if(userRepository.existsByEmail(request.getEmail())){
            throw new EmailAlreadyExistsException("Email already registered");
        }
        
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new PasswordMismatchException("Password and Confirm Password do not match");
        }
        
        // Determine role
        Role role;
        String requestedRole = request.getRole();
        if("SELLER".equalsIgnoreCase(requestedRole)) {
            role = Role.SELLER;
        } else if ("BUYER".equalsIgnoreCase(requestedRole)) {
            role = Role.BUYER;
        } else {
            throw new RuntimeException("Invalid role! Please select role as SELLER or BUYER");
        }

        // Create user
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setMobileNumber(request.getMobileNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        user.setActive(false);
        
        userRepository.save(user);
        
        notificationService.notifyAdmins(
        	    "New User Registered",
        	    "User '" + user.getName() + "' registered as " + user.getRole(),
        	    NotificationType.SYSTEM
        	); 
        
        Token token = tokenService.createToken(user, TokenType.EMAIL_VERIFICATION, EMAIL_VERIFICATION_HOURS);
        String verificationLink = "http://localhost:8080/api/verify?token=" + token.getToken();
        
        emailService.sendVerificationEmail(user.getEmail(), token.getToken());
        
        AuthResponseDto data = new AuthResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                null
        );
        // Return response DTO
        return new ApiResponse<>(
        		true,
        		"Registration successful. Please verify your email."
        		, data);
    }
    
    @Override
	public User verifyEmail(String tokenStr) {
    	Token token = tokenService.validateToken(tokenStr, TokenType.EMAIL_VERIFICATION);
        User user = token.getUser();

        if (user.isActive()) {
            throw new InvalidTokenException("Account already verified");
        }

        user.setActive(true);
        tokenService.deleteToken(token);
        return userRepository.save(user);
	}

    @Override
    public ApiResponse login(LoginRequestDto request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        
        if(!user.isActive()) {
        	throw new AccountNotVerifiedException("Please verify your email before logging in.");
        }
        
//        if (!user.getRole().name().equalsIgnoreCase(request.getRole())) {
//            throw new InvalidCredentialsException("Invalid role for this user");
//        }
        
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (BadCredentialsException ex) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        // Generate JWT
        String token = jwtUtil.generateToken(user.getEmail());
        
        AuthResponseDto data = new AuthResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                token
        );

        return new ApiResponse<>(true, "Login successful", data);
    }

    @Override
    public ApiResponse<?> forgotPassword(ForgotPasswordRequestDto request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Token token = tokenService.createToken(user, TokenType.RESET_PASSWORD, PASSWORD_RESET_HOURS);

        // ✅ FRONTEND LINK (IMPORTANT)
        String resetLink = "http://localhost:5501/Reset_Password.html?token=" + token.getToken();

        emailService.sendResetPasswordEmail(
                user.getEmail(),
                "Password Reset",
                "Click here to reset your password: " + resetLink
        );

        return new ApiResponse<>(true, "Password reset link sent to email", null);
    }

    @Override
    public ApiResponse<?> resetPassword(ResetPasswordDto request) {
    	Token token = tokenService.validateToken(request.getToken(), TokenType.RESET_PASSWORD);
        
    	User user = token.getUser();

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new PasswordMismatchException("New password and confirm password do not match");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        tokenService.deleteToken(token);

        return new ApiResponse<>(true, "Password reset successful", null);
    }
    
    //new added -09-04-2026
    @Override
    public void validateResetToken(String token) {
        tokenService.validateToken(token, TokenType.RESET_PASSWORD);
    }
    
    //new added -09-04-2026
    @Override
    public ApiResponse<?> resendVerificationEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (user.isActive()) {
            return new ApiResponse<>(false, "Account already verified", null);
        }
        
        // Optional: Delete old verification tokens
        tokenService.deleteExistingTokens(user, TokenType.EMAIL_VERIFICATION);

        // Create a new verification token
        Token token = tokenService.createToken(user, TokenType.EMAIL_VERIFICATION, EMAIL_VERIFICATION_HOURS);

        // Send verification email
        emailService.sendVerificationEmail(user.getEmail(), token.getToken());

        return new ApiResponse<>(true, "Verification link resent successfully", null);
    }

	
}