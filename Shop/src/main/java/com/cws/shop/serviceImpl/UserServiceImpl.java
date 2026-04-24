package com.cws.shop.serviceImpl;

import org.springframework.stereotype.Service;

import com.cws.shop.dto.response.UserDto;
import com.cws.shop.exception.UserNotFoundException;
import com.cws.shop.model.User;
import com.cws.shop.repository.UserRepository;
import com.cws.shop.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	private UserRepository userRepository;
	
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDto getUserByEmail(String email) {
		User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

		return new UserDto(
	            user.getId(),
	            user.getName(),   
	            user.getEmail(),
	            user.getRole()
	    );
    }
}
