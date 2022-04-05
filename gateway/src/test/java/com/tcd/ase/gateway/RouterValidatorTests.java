package com.tcd.ase.gateway;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;

@SpringBootTest
public class RouterValidatorTests {

    @Test
	void testInsecureUrl() {
        MockServerHttpRequest fakeReq = MockServerHttpRequest.get("foo/bar").build();
        RouterValidator router = new RouterValidator();
        boolean resp = router.useSecurity(fakeReq);
        assertTrue(resp);
	}

    @Test
	void testSecureUrl() {
        RouterValidator router = new RouterValidator();
        MockServerHttpRequest fakeReq = MockServerHttpRequest.get("/user/register").build();
        boolean resp = router.useSecurity(fakeReq);
        assertFalse(resp);
	}

}
