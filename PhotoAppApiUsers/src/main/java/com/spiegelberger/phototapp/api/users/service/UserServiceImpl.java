package com.spiegelberger.phototapp.api.users.service;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spiegelberger.phototapp.api.users.data.UserEntity;
import com.spiegelberger.phototapp.api.users.data.UserRepository;
import com.spiegelberger.phototapp.api.users.shared.UserDto;

@Service
public class UserServiceImpl implements UserService{
	
	UserRepository userRepository;
		
	@Autowired
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}



	@Override
	public UserDto createUser(UserDto userDetails) {
		userDetails.setUserId(UUID.randomUUID().toString());
		
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		//Converting dto object into entity object:
		UserEntity userEntity=modelMapper.map(userDetails, UserEntity.class);
		
		//temporal solution:
		userEntity.setEncryptedPassword("test");
		
		userRepository.save(userEntity);
		return null;
	}

}
