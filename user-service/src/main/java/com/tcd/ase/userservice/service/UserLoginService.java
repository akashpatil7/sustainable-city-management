package com.tcd.ase.userservice.service;

import com.tcd.ase.userservice.entity.User;
import com.tcd.ase.userservice.models.UserLoginRequest;
import com.tcd.ase.userservice.repository.UserRepository;
import com.tcd.ase.utils.JWTokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserLoginService {

    @Autowired
    private UserRepository repository;

    public ResponseEntity<String> login(UserLoginRequest request) {
        JWTokenHelper helper = new JWTokenHelper();
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
            return ResponseEntity.status(HttpStatus.OK).body(token);
        }
    }
}
