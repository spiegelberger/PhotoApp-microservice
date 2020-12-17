package com.spiegelberger.phototapp.api.users.service;

import java.util.UUID;

import com.spiegelberger.phototapp.api.users.shared.UserDto;

public class UserServiceImpl implements UserService{

	@Override
	public UserDto createUser(UserDto userDetails) {
		userDetails.setUserId(UUID.randomUUID().toString());
		return null;
	}

}
