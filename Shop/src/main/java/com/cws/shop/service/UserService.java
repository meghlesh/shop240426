package com.cws.shop.service;

import com.cws.shop.dto.response.UserDto;

public interface UserService {
	UserDto getUserByEmail(String email);
}
