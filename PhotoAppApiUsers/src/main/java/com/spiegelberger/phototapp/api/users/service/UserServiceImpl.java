package com.spiegelberger.phototapp.api.users.service;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.spiegelberger.phototapp.api.users.data.UserEntity;
import com.spiegelberger.phototapp.api.users.data.UserRepository;
import com.spiegelberger.phototapp.api.users.shared.UserDto;

@Service
public class UserServiceImpl implements UserService{
	
	UserRepository userRepository;
	BCryptPasswordEncoder bCryptPasswordEncoder;
		
	@Autowired
	public UserServiceImpl(UserRepository userRepository, 
					BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}



	@Override
	public UserDto createUser(UserDto userDetails) {
		
		//Set user's missing informations:
		userDetails.setUserId(UUID.randomUUID().toString());		
		userDetails.setEncryptedPassword(
				bCryptPasswordEncoder.encode(userDetails.getPassword()));
		
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		//Converting dto object into entity object:
		UserEntity userEntity=modelMapper.map(userDetails, UserEntity.class);
		
		userRepository.save(userEntity);
		
		//Converting entity object into dto object:
		UserDto returnValue = modelMapper.map(userEntity, UserDto.class);
		
		return returnValue;
	}

}
