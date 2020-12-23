package com.spiegelberger.phototapp.api.users.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.spiegelberger.phototapp.api.users.data.AlbumsServiceClient;
import com.spiegelberger.phototapp.api.users.data.UserEntity;
import com.spiegelberger.phototapp.api.users.data.UserRepository;
import com.spiegelberger.phototapp.api.users.shared.UserDto;
import com.spiegelberger.phototapp.api.users.ui.model.AlbumResponseModel;

@Service
public class UserServiceImpl implements UserService {

	UserRepository userRepository;
	BCryptPasswordEncoder bCryptPasswordEncoder;
	Environment env;
	AlbumsServiceClient albumsServiceClient;
//  Use this only if you do not use FeignClient:
//	RestTemplate restTemplate;
	

	@Autowired
	public UserServiceImpl(UserRepository userRepository, Environment env,
			BCryptPasswordEncoder bCryptPasswordEncoder, AlbumsServiceClient albumsServiceClient
//			RestTemplate restTemplate
			) {
		this.userRepository = userRepository;
		this.env = env;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.albumsServiceClient = albumsServiceClient;
//		this.restTemplate = restTemplate;
	}

	@Override
	public UserDto createUser(UserDto userDetails) {

		// Set user's missing informations:
		userDetails.setUserId(UUID.randomUUID().toString());
		userDetails.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		// Converting dto object into entity object:
		UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);

		userRepository.save(userEntity);

		// Converting entity object into dto object:
		UserDto returnValue = modelMapper.map(userEntity, UserDto.class);

		return returnValue;
	}

	/*
	 * This method is used by Spring security during Authentication process
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		UserEntity userEntity = userRepository.findByEmail(username);
		if (userEntity == null) {
			throw new UsernameNotFoundException(username);
		}

		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), true, true, true, true,
				new ArrayList<>());
	}

	/*
	 * This method is used by Spring security during Login
	 */
	@Override
	public UserDto getUserDetailsByEmail(String email) {

		UserEntity userEntity = userRepository.findByEmail(email);
		if (userEntity == null) {
			throw new UsernameNotFoundException(email);
		}
		return new ModelMapper().map(userEntity, UserDto.class);
	}

	@Override
	public UserDto getUserByUserId(String userId) {

		UserEntity userEntity = userRepository.findByUserId(userId);
		if (userEntity == null) {
			throw new UsernameNotFoundException("User not found");
		}

		UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

//	     Use these lines with RestTemplate:		
//		String albumsUrl = String.format(env.getProperty("albums.url"), userId);
//		ResponseEntity<List<AlbumResponseModel>> albumsListResponse = restTemplate.exchange(albumsUrl, HttpMethod.GET,
//				null, new ParameterizedTypeReference<List<AlbumResponseModel>>() {
//				});
//		List<AlbumResponseModel> albumsList = albumsListResponse.getBody();
		
		
//		And this line with FeignClient:
		List<AlbumResponseModel> albumsList = albumsServiceClient.getAlbums(userId);
		
		userDto.setAlbumsList(albumsList);

		return userDto;
	}

}
