package com.tcd.ase.userservice.controller;

import com.nimbusds.jose.shaded.json.JSONObject;
import com.tcd.ase.userservice.models.UserLoginRequest;
import com.tcd.ase.userservice.models.UserRegistrationRequest;
import com.tcd.ase.userservice.service.UserLoginService;
import com.tcd.ase.userservice.service.UserRegistrationService;
  
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserServiceControllerImpl implements UserServiceController {

	@Autowired
	UserLoginService userLoginService;

	@Autowired
	UserRegistrationService userRegistrationService;

	@Override
	public ResponseEntity<JSONObject> login(UserLoginRequest request) {

		return userLoginService.login(request);
	}

	@Override
	public ResponseEntity<Void> register(UserRegistrationRequest request) {

		return userRegistrationService.register(request);
	}


}
