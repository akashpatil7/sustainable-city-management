package com.tcd.ase.userservice.service;

import java.util.Optional;

import com.tcd.ase.userservice.entity.User;
import com.tcd.ase.userservice.models.UserRegistrationRequest;
import com.tcd.ase.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationService {

    @Autowired
    private UserRepository repository;

    public ResponseEntity<Object> register(UserRegistrationRequest request) {
        UserMapper mapper = new UserMapper();
        ResponseEntity<Object> response = null;
        User user = mapper.fromRegistrationRequestToEntity(request);
        Optional<User> existingUser = repository.findById(request.getEmail());
        if(existingUser.isPresent()) {
            return new ResponseEntity<Object>("A user with that email already exists.", HttpStatus.FORBIDDEN);
        }
        try {
            repository.save(user);
        }
        catch(Exception e){
            return new ResponseEntity<Object>("There was an error creating the account.", HttpStatus.FORBIDDEN);
        }
        response = new ResponseEntity<Object>(HttpStatus.OK);
        return response;
    }
}
