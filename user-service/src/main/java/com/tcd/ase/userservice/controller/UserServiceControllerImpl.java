package com.tcd.ase.userservice.controller;

import com.nimbusds.jose.shaded.json.JSONObject;
import com.tcd.ase.userservice.entity.User;
import com.tcd.ase.userservice.models.UserLoginRequest;
import com.tcd.ase.userservice.models.UserRegistrationRequest;
import com.tcd.ase.userservice.repository.UserRepository;
import com.tcd.ase.userservice.service.UserLoginService;
import com.tcd.ase.userservice.service.UserMapper;
import com.tcd.ase.userservice.service.UserRegistrationService;
import com.tcd.ase.utils.JWTokenHelper;
  
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class UserServiceControllerImpl implements UserServiceController {

	@Autowired
	UserLoginService userLoginService;

	@Autowired
	UserRegistrationService userRegistrationService;

	@Override
	public ResponseEntity<Object> login(UserLoginRequest request) {

		return userLoginService.login(request);
	}

	@Override
	public ResponseEntity<Object> register(UserRegistrationRequest request) {

		return userRegistrationService.register(request);
	}


}
