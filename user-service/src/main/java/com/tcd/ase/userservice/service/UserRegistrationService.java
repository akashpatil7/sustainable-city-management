package com.tcd.ase.userservice.service;

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

    public ResponseEntity<Void> register(UserRegistrationRequest request) {
        UserMapper mapper = new UserMapper();
        ResponseEntity<Void> response = null;
        User user = mapper.fromRegistrationRequestToEntity(request);
        try {
            repository.save(user);
        }
        catch(Exception e){
            System.out.println(e);
        }
        response = new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        return response;
    }
}
