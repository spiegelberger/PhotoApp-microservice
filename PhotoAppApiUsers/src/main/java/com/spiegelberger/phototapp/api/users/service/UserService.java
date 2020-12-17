package com.spiegelberger.phototapp.api.users.service;

import com.spiegelberger.phototapp.api.users.shared.UserDto;

public interface UserService {

	UserDto createUser(UserDto userDetails);
	
}
