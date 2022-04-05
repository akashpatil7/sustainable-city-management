package com.tcd.ase.gateway;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Arrays;

@Component
public class RouterValidator {

    public static final List<String> openApiEndpoints = Arrays.asList(new String[]{
                "/user/register",
                "/user/login"
    });

    public boolean useSecurity(ServerHttpRequest request) {
        return openApiEndpoints.stream()
                .noneMatch(uri -> request.getURI().getPath().contains(uri));
    }

}