package com.tcd.ase.userservice.service;

import com.tcd.ase.userservice.entity.User;
import com.tcd.ase.userservice.models.UserLoginRequest;
import com.tcd.ase.userservice.models.UserLoginResponse;
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

    public ResponseEntity<Object> login(UserLoginRequest request) {
        JWTokenHelper helper = new JWTokenHelper();
        final String token;
        Optional<User> user = repository.findById(request.getEmail());
        if(!user.isPresent()) {
            return new ResponseEntity<Object>("User does not exist",HttpStatus.FORBIDDEN);
        }
        else {
            User ruser = user.get();
            if(!request.getPassword().equals(ruser.getPassword())) {
                return new ResponseEntity<Object>("Incorrect password", HttpStatus.FORBIDDEN);
            }
            token = helper.generateToken(ruser.getUserName());
            UserLoginResponse resp = new UserLoginResponse();
            resp.setToken(token);
            return new ResponseEntity<Object>(resp,HttpStatus.OK);
        }
    }
}
