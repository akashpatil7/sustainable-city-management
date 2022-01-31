package com.tcd.ase.userservice.service;

import com.nimbusds.jose.shaded.json.JSONObject;
import com.tcd.ase.userservice.entity.User;
import com.tcd.ase.userservice.models.UserLoginRequest;
import com.tcd.ase.userservice.repository.UserRepository;
import com.tcd.ase.utils.JWTokenHelper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class UserLoginServiceTest {

    @InjectMocks
    UserLoginService userLoginService;

    @Mock
    UserRepository userRepository;

    @Mock
    JWTokenHelper jwTokenHelper;

    @Before
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLogin(){
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setEmail("admin@admin");
        userLoginRequest.setPassword("admin");

        User user = new User();
        user.setPassword("admin");
        user.setUserName("admin");

        com.nimbusds.jose.shaded.json.JSONObject body=new JSONObject();
        body.put("token","12345");

        when(userRepository.findById(user.getUserName())).thenReturn(Optional.of(user));
        when(jwTokenHelper.generateToken(user.getUserName())).thenReturn("12345");

        ResponseEntity<JSONObject> token = userLoginService.login(userLoginRequest);
        assertNotNull(token.getBody());
    }

    @Test
    public void testLoginUserNotFound(){
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setEmail("admin@admin");
        userLoginRequest.setPassword("admin");

        User user = new User();
        when(userRepository.findById(userLoginRequest.getEmail())).thenReturn(Optional.of(user));

        ResponseEntity<JSONObject> token = userLoginService.login(userLoginRequest);
        assertNotNull(token.getBody());
    }
}
