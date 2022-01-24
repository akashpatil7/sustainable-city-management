package com.tcd.ase.userservice.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.tcd.ase.userservice.entity.User;
import com.tcd.ase.userservice.models.UserLoginRequest;
import com.tcd.ase.userservice.models.UserRegistrationRequest;
import com.tcd.ase.userservice.repository.UserRepository;
import com.tcd.ase.userservice.service.UserMapper;
import com.tcd.ase.utils.JWTokenHelper;

@RestController
public class UserServiceControllerImpl implements UserServiceController {
	
	@Autowired
	private UserRepository repository;

	@Override
	public ResponseEntity<String> login(UserLoginRequest request) {
		JWTokenHelper helper = new JWTokenHelper();
		final ResponseEntity<String> response;
		final String token;
		Optional<User> user = repository.findById(request.getEmail());
		if(user.isEmpty()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid username or password");
		}
		else {
			User ruser = user.get();
			if(!request.getPassword().equals(ruser.getPassword())) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid username or password");
			}
			 token = helper.generateToken(ruser.getUser_name());
			return ResponseEntity.status(HttpStatus.OK).body(token);
		}
		
	}

	@Override
	public ResponseEntity<Void> register(UserRegistrationRequest request) {
		UserMapper mapper = new UserMapper();
		ResponseEntity<Void> response = null;
		User user = mapper.fromRegistrationRequestToEntity(request);
		repository.save(user);
		response = new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		return response;
	}


}
