package com.tcd.ase.gateway;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.tcd.ase.utils.JWTokenHelper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;

@SpringBootTest
public class AuthentificationFilterTests {
    @Test
    void testGetToken() {
        String validToken = "Bearer= 12345";
        String noBearerToken = "12345";
        String invalidToken = "Bearer= invalid";

        AuthenticationFilter filter = new AuthenticationFilter();
        JWTokenHelper helper = mock(JWTokenHelper.class);
        when(helper.isValid(noBearerToken)).thenReturn(true); 

        String resp = filter.getToken(validToken, helper);
        assertEquals(resp, noBearerToken);

        resp = filter.getToken(invalidToken, helper);
        assertEquals(resp, "");

        resp = filter.getToken(noBearerToken, helper);
        assertEquals(resp, "");
    }

    @Test
    void testHasParamAuthorization() {
        String token = "12345";
        MockServerHttpRequest noAuthReq = MockServerHttpRequest.get("foo/bar").queryParam("Invalid", "12345").build();
        MockServerHttpRequest invalidReq = MockServerHttpRequest.get("foo/bar").queryParam("Authorization", "invalid").build();
        MockServerHttpRequest validReq = MockServerHttpRequest.get("foo/bar").queryParam("Authorization", "12345").build();
 
        JWTokenHelper helper = mock(JWTokenHelper.class);
        when(helper.isValid(token)).thenReturn(true); 
        AuthenticationFilter filter = new AuthenticationFilter();

        String resp = filter.getTokenFromQueryParam(noAuthReq, helper);
        assertEquals(resp,"");

        resp = filter.getTokenFromQueryParam(invalidReq, helper);
        assertEquals(resp,"");

        resp = filter.getTokenFromQueryParam(validReq, helper);
        assertNotEquals(resp,"");
    }
}
