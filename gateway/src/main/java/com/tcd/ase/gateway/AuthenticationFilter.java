package com.tcd.ase.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.tcd.ase.utils.JWTokenHelper;

import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter implements GatewayFilter {
	@Autowired
    private RouterValidator routerValidator;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		ServerHttpResponse response = exchange.getResponse();
		JWTokenHelper helper = new JWTokenHelper();
		final String token;
		if (routerValidator.isSecured.test(request)) {
			if (!request.getHeaders().containsKey("Authorization")) {
				response.setStatusCode(HttpStatus.UNAUTHORIZED);
				return response.setComplete();
			}
			String header = request.getHeaders().getFirst("Authorization");
			if (header != null && header.contains("Bearer")) {
				token = header.split(" ")[1].trim();
				if (helper.isValid(token)) {
					exchange.getRequest().mutate().header("user", helper.getUser(token)).build();
				} else {
					response.setStatusCode(HttpStatus.UNAUTHORIZED);
					return response.setComplete();
				}
			} else {
				response.setStatusCode(HttpStatus.UNAUTHORIZED);
				return response.setComplete();
			}
		}
		return chain.filter(exchange);
	}
}