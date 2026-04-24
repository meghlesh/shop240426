package com.cws.shop.controller;

import com.cws.shop.dto.request.ForgotPasswordRequestDto;
import com.cws.shop.dto.request.LoginRequestDto;
import com.cws.shop.dto.request.RegisterRequestDto;
import com.cws.shop.dto.request.ResetPasswordDto;
import com.cws.shop.dto.response.ApiResponse;

import java.io.PrintWriter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cws.shop.service.AuthService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequestDto request){
        ApiResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login (@Valid @RequestBody LoginRequestDto request){
    	ApiResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify")
    public ResponseEntity<ApiResponse<String>> verify(@RequestParam("token") String token){
    	authService.verifyEmail(token);
    	
    	ApiResponse<String> apiResponse = 
    			new ApiResponse<String>(true, "Account Verified Successfully", token);
    	
    	return ResponseEntity.ok(apiResponse); 
    }
    
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<?>> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDto request){
        ApiResponse<?> response = authService.forgotPassword(request);
        return ResponseEntity.ok(response);
    }
   
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<?>> resetPassword(@Valid @RequestBody ResetPasswordDto request){
        ApiResponse<?> response = authService.resetPassword(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/resend-verification")
    public ResponseEntity<ApiResponse<?>> resendVerification(@RequestBody @Valid ForgotPasswordRequestDto request) {
        ApiResponse<?> response = authService.resendVerificationEmail(request.getEmail());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/reset-password-page")
    public void handleResetPasswordPage(@RequestParam("token") String token, HttpServletResponse response) {
        try {
            // Validate token (throws exception if invalid)
            authService.validateResetToken(token);

            // Token is valid → redirect to frontend reset password page
            response.sendRedirect("http://localhost:3000/Reset_Password.html?token=" + token);

        } catch (Exception ex) {
            // Token invalid or expired → simple text message
            response.setContentType("text/html");
            try (PrintWriter out= response.getWriter()) {
                out.println("Invalid or expired link");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}