package com.tcd.ase.userservice.controller;

import com.nimbusds.jose.shaded.json.JSONObject;
import com.tcd.ase.userservice.models.UserLoginRequest;
import com.tcd.ase.userservice.models.UserRegistrationRequest;
import com.tcd.ase.userservice.service.UserLoginService;
import com.tcd.ase.userservice.service.UserRegistrationService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.Assert.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserServiceControllerImplTest {

    @InjectMocks
    UserServiceControllerImpl userServiceController = new UserServiceControllerImpl();

    @Mock
    UserLoginService userLoginService;

    @Mock
    UserRegistrationService userRegistrationService;

    @Before
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLogin(){
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setEmail("admin@admin");
        userLoginRequest.setPassword("admin");

        JSONObject body=new JSONObject();
        body.put("token","12345");
        when(userLoginService.login(userLoginRequest))
                .thenReturn((ResponseEntity<JSONObject>) ResponseEntity.status(HttpStatus.OK).body(body));

        ResponseEntity<JSONObject> response = userServiceController.login(userLoginRequest);
        assertNotNull(response.getBody());
    }

    @Test
    public void testRegister(){
        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest();
        userRegistrationRequest.setEmail("admin@admin");
        userRegistrationRequest.setPassword("admin");
        userRegistrationRequest.setUsername("admin");

        when(userRegistrationService.register(userRegistrationRequest))
                .thenReturn(any(ResponseEntity.class));

        ResponseEntity<Void> response = userServiceController.register(userRegistrationRequest);
    }

}
