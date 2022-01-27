package com.tcd.ase.userservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.tcd.ase.userservice.models.UserLoginRequest;
import com.tcd.ase.userservice.models.UserRegistrationRequest;

public interface UserServiceController {

	String BASE_URL = "/user";

	@PostMapping(path = BASE_URL + "/register")
	public ResponseEntity<Void> register(@RequestBody UserRegistrationRequest request);

	@PostMapping(path = BASE_URL + "/login")
	public ResponseEntity<String> login(@RequestBody UserLoginRequest request);

}
