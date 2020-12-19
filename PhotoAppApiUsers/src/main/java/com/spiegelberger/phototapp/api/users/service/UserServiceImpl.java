package com.spiegelberger.phototapp.api.users.service;

import java.util.ArrayList;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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


	/*
	 * This method is used by Spring security during Authentication process
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UserEntity userEntity = userRepository.findByEmail(username);
			if(userEntity==null) {
				throw new UsernameNotFoundException(username);
			}
		
		return new User(
		  userEntity.getEmail(), userEntity.getEncryptedPassword(), true, true, true, true, new ArrayList<>() );
	}

	
	/*
	 * This method is used by Spring security during Login 
	 */
	@Override
	public UserDto getUserDetailsByEmail(String email) {
		
		UserEntity userEntity = userRepository.findByEmail(email);
		if(userEntity==null) {
			throw new UsernameNotFoundException(email);
		}
		return new ModelMapper().map(userEntity, UserDto.class);
	}

}
