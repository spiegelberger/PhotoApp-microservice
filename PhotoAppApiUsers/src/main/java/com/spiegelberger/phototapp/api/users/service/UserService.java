package com.spiegelberger.phototapp.api.users.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.spiegelberger.phototapp.api.users.shared.UserDto;

public interface UserService extends UserDetailsService {

	UserDto createUser(UserDto userDetails);

	UserDto getUserDetailsByEmail(String email);

	UserDto getUserByUserId(String userId);
	
}
