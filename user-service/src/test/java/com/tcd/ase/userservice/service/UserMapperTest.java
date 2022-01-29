package com.tcd.ase.userservice.service;

import com.tcd.ase.userservice.entity.User;
import com.tcd.ase.userservice.models.UserRegistrationRequest;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class UserMapperTest {

    @Mock
    UserMapper userMapper = new UserMapper();

    @InjectMocks
    UserMapperTest userMapperTest;

    @Test
    public void testFromRegistrationRequestToEntity() throws Exception {
        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest();
        userRegistrationRequest.setUsername("admin");
        userRegistrationRequest.setPassword("admin");
        userRegistrationRequest.setEmail("admin@admin");

        User user;
        user = userMapper.fromRegistrationRequestToEntity(userRegistrationRequest);
        assertNotNull(user.getUserEmail());
        assertNotNull(user.getUserName());
        assertNotNull(user.getPassword());
    }


}

