package com.tcd.ase.userservice.service;

import com.tcd.ase.userservice.models.UserRegistrationRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tcd.ase.userservice.repository.UserRepository;

import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertNotNull;
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
        request.setEmail("admin@admin");
        request.setPassword("admin");

        ResponseEntity response = userRegistrationService.register(request);
        assertNotNull(response);
    }

    @Test(expected = Exception.class)
    public void testRegisterFailure(){
        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest();
        userRegistrationRequest.setEmail("admin@admin");
        userRegistrationRequest.setPassword("admin");
        userRegistrationRequest.setUsername("admin");

        when(userRegistrationService.register(userRegistrationRequest)).thenThrow(Exception.class);

        ResponseEntity response = userRegistrationService.register(userRegistrationRequest);
    }

}
