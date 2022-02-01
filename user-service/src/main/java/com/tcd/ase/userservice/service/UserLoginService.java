package com.tcd.ase.userservice.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tcd.ase.userservice.entity.User;
import com.tcd.ase.userservice.models.UserLoginRequest;
import com.tcd.ase.userservice.models.UserLoginResponse;
import com.tcd.ase.userservice.repository.UserRepository;
import com.tcd.ase.utils.JWTokenHelper;


@Service
public class UserLoginService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserLoginService.class);

    @Autowired
    private UserRepository repository;

    public ResponseEntity<Object> login(UserLoginRequest request) {
    	logger.info("Processing login request");
        JWTokenHelper helper = new JWTokenHelper();
        UserLoginResponse response = new UserLoginResponse();
        final String token;
        Optional<User> user = repository.findById(request.getEmail());
        if(!user.isPresent()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid username or password");
        }
        else {
            User ruser = user.get();
            if(!request.getPassword().equals(ruser.getPassword())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid username or password");
            }
            token = helper.generateToken(ruser.getUserName());
            if(token == null || token.isEmpty()) {
            	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("System cannot process request at this time");
            }
            response.setToken(token);
            return new ResponseEntity<Object>(response, HttpStatus.CREATED);
        }
    }
}
