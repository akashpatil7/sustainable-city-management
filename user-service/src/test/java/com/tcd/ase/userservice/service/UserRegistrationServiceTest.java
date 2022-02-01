package com.tcd.ase.userservice.service;

import com.tcd.ase.userservice.models.UserRegistrationRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tcd.ase.userservice.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class UserRegistrationServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserRegistrationService userRegistrationService;

    @Before
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegister(){
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setUsername("admin");
        request.setEmail("admin@dublincity.ie");
        request.setPassword("admin");

        ResponseEntity<Object> response = userRegistrationService.register(request);
        assertTrue(response.getStatusCode().is2xxSuccessful());

    }

    @Test(expected = Exception.class)
    public void testRegisterFailure(){
        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest();
        userRegistrationRequest.setEmail("admin@dublincity.ie");
        userRegistrationRequest.setPassword("admin");
        userRegistrationRequest.setUsername("admin");

        when(userRegistrationService.register(userRegistrationRequest)).thenThrow(Exception.class);

        ResponseEntity<Object> response = userRegistrationService.register(userRegistrationRequest);
        assertTrue(response.getStatusCode().is4xxClientError());
    }

    @Test
    public void testRegisterFailureInvalidEmail(){
        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest();
        userRegistrationRequest.setEmail("admin@admin");
        userRegistrationRequest.setPassword("admin");
        userRegistrationRequest.setUsername("admin");

        ResponseEntity<Object> response = userRegistrationService.register(userRegistrationRequest);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testRegisterFailureEmptyRequest(){
        UserRegistrationRequest userRegistrationRequest = null;

        ResponseEntity<Object> response = userRegistrationService.register(userRegistrationRequest);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

}
