package com.tcd.ase.userservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tcd.ase.userservice.entity.User;
import com.tcd.ase.userservice.models.UserRegistrationRequest;
import com.tcd.ase.userservice.repository.UserRepository;

@Service
public class UserRegistrationService {
	private static final Logger logger = LoggerFactory.getLogger(UserRegistrationService.class);

    @Autowired
    private UserRepository repository;

    public ResponseEntity<Object> register(UserRegistrationRequest request) {
        if(request == null ||
                !request.getEmail().matches("^[a-zA-Z0-9_.+-]+@dublincity\\.ie$")){
            return new ResponseEntity<>("Invalid request",HttpStatus.BAD_REQUEST);
        }
        UserMapper mapper = new UserMapper();
        User user = mapper.fromRegistrationRequestToEntity(request);
        try {
            repository.save(user);
        }
        catch(Exception e) {
        	logger.error("Error in UserRegistrationService register method", e);
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Object>("Registration successful",HttpStatus.OK);
    }
}
