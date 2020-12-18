package com.spiegelberger.phototapp.api.users.ui.controllers;

import javax.validation.Valid;
import org.springframework.http.MediaType;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spiegelberger.phototapp.api.users.service.UserService;
import com.spiegelberger.phototapp.api.users.shared.UserDto;
import com.spiegelberger.phototapp.api.users.ui.model.CreateUserRequestModel;
import com.spiegelberger.phototapp.api.users.ui.model.CreateUserResponseModel;



@RestController
@RequestMapping("/users")
public class UsersController {

	@Autowired
	private Environment env;
	
	@Autowired
	UserService userService;
		
	
	@GetMapping("/status/check")
	public String status() {
		return "Users microservice is working on port " + env.getProperty("local.server.port");
	}
	
	
	@PostMapping (
			consumes= {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
			produces ={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<CreateUserResponseModel>createUser(@Valid @RequestBody CreateUserRequestModel userDetails) {
		
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		//Converting request object into dto object:
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);
		
		UserDto createdUser=userService.createUser(userDto);		
		
		//Converting dto object into response object:
		CreateUserResponseModel returnValue =
				modelMapper.map(createdUser, CreateUserResponseModel.class);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
	}
}
