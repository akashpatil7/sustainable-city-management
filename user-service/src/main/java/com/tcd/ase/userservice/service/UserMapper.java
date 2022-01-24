package com.tcd.ase.userservice.service;

import com.tcd.ase.userservice.entity.User;
import com.tcd.ase.userservice.models.UserRegistrationRequest;

public class UserMapper {
    public User fromRegistrationRequestToEntity(UserRegistrationRequest reqeust) {
    	User user = new User();
    	user.setPassword(reqeust.getPassword());
    	user.setUser_email(reqeust.getEmail());
    	user.setUser_name(reqeust.getUsername());
    	return user;
    }
}
