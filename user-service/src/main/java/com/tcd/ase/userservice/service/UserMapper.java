package com.tcd.ase.userservice.service;

import com.tcd.ase.userservice.entity.User;
import com.tcd.ase.userservice.models.UserRegistrationRequest;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class UserMapper {
    public User fromRegistrationRequestToEntity(UserRegistrationRequest request) throws InvalidKeySpecException, NoSuchAlgorithmException {
		PasswordHashing hashing = new PasswordHashing();
    	User user = new User();
		user.setSalt(hashing.generateSalt());
    	user.setPassword(hashing.hash(request.getPassword(), user.getSalt()));
    	user.setUserEmail(request.getEmail());
    	user.setUserName(request.getUsername());
    	return user;
    }
}
