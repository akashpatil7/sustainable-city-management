package com.tcd.ase.userservice.service;

import com.nimbusds.jose.shaded.json.JSONObject;
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

    public ResponseEntity<JSONObject> login(UserLoginRequest request) {
        JWTokenHelper helper = new JWTokenHelper();
        final String token;
        JSONObject body=new JSONObject();
        Optional<User> user = repository.findById(request.getEmail());
        if(!user.isPresent()) {
            return new ResponseEntity<JSONObject>(body,HttpStatus.FORBIDDEN);
        }
        else {
            User ruser = user.get();
            if(!request.getPassword().equals(ruser.getPassword())) {
                return new ResponseEntity<JSONObject>(body, HttpStatus.FORBIDDEN);
            }
            token = helper.generateToken(ruser.getUserName());
            body.put("token", token);
            return new ResponseEntity<JSONObject>(body,HttpStatus.OK);
        }
    }
}
