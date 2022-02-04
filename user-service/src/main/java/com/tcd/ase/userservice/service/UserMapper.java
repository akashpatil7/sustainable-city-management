package com.tcd.ase.userservice.service;

import com.tcd.ase.userservice.entity.User;
import com.tcd.ase.userservice.models.UserRegistrationRequest;

public class UserMapper {
    public User fromRegistrationRequestToEntity(UserRegistrationRequest request) {
    	User user = new User();
    	user.setPassword(request.getPassword());
    	user.setUserEmail(request.getEmail());
    	user.setUserName(request.getUsername());
    	return user;
    }
}
