package com.tcd.ase.utils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JWTokenHelperTests {
    @InjectMocks
    JWTokenHelper helper;

    @Mock
    JWTClaimsSet claimset;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testIsValid() {
        String validToken = helper.generateToken("test");
        String invalidToken = "test";
        String emptyToken = null;

        boolean isValid = helper.isValid(validToken);
        assertTrue(isValid);

        isValid = helper.isValid(invalidToken);
        assertFalse(isValid);

        isValid = helper.isValid(emptyToken);
        assertThrows(
                ParseException.class,
                () -> helper.isValid(emptyToken));
    }

    @Test
    public void testGetUser() {
        String validToken = helper.generateToken("test");
        when(claimset.getSubject()).thenReturn("test");
        String user = helper.getUser(validToken);
        assertEquals(user, "test");

        String invalidToken = "";
        assertThrows(
                ParseException.class,
                () -> helper.getUser(invalidToken));

    }

    @Test
    public void testGenerateToken() {
        String user = "test";
        String invalid = null;
        String token = helper.generateToken(user);
        assertFalse(token.isEmpty());

        assertThrows(
                JOSEException.class,
                () -> helper.getUser(invalid));
    }
}
