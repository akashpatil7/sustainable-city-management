package com.tcd.ase.userservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.tcd.ase.userservice.models.UserLoginRequest;
import com.tcd.ase.userservice.models.UserRegistrationRequest;

public interface UserServiceController {
	
	@PostMapping(path = "/user/register")
	public ResponseEntity<Void> register(@RequestBody UserRegistrationRequest request);
	@PostMapping(path = "/user/login")
	public ResponseEntity<String> login(@RequestBody UserLoginRequest request);

}
