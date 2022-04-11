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
        final String token;
        Optional<User> user = repository.findById(request.getEmail());
        if(!user.isPresent()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User does not exist");
        }
        else {
            try {
                User ruser = user.get();
                PasswordHashing hashing = new PasswordHashing();
                if (!hashing.match(request.getPassword(), ruser.getPassword(), ruser.getSalt())) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Incorrect password");
                }
                token = helper.generateToken(ruser.getUserName());
                if (token == null || token.isEmpty()) {
                    throw new Exception("System cannot process request at this time");
                }
                UserLoginResponse response = new UserLoginResponse();
                response.setToken(token);
                return new ResponseEntity<Object>(response, HttpStatus.OK);
            } catch (Exception e) {
                logger.error("Error in UserRegistrationService register method", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
        }
    }
}
