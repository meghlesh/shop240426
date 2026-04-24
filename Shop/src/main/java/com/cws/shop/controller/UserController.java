package com.cws.shop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cws.shop.dto.response.UserDto;
import com.cws.shop.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
        private UserService userService;
        
	    public UserController(UserService userService) {
			this.userService = userService;
		}

		@GetMapping("/me")
	    public ResponseEntity<UserDto> getCurrentUser(Authentication authentication) {
	        // authentication.getName() returns the email you put in the Token Subject
	        String email = authentication.getName();

	        // Fetch the DTO from your service
	        UserDto userDto = userService.getUserByEmail(email);

	        return ResponseEntity.ok(userDto);
	    
	}
}
