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
		String token = "";
		if (routerValidator.useSecurity(request)) {
			System.out.println("Needs security checks");
			if (request.getHeaders().containsKey("Authorization") || request.getQueryParams().containsKey("Authorization")) {
				if(request.getHeaders().containsKey("Authorization")) {
					String header = request.getHeaders().getFirst("Authorization");
					token = getToken(header, helper);
				}
				else {
					token = getTokenFromQueryParam(request, helper);
				}
				if (token != "") {
					exchange.getRequest().mutate().header("user", helper.getUser(token)).build();
				}
				else {
					response.setStatusCode(HttpStatus.UNAUTHORIZED);
					return response.setComplete();
				}
			}
			else {
				response.setStatusCode(HttpStatus.UNAUTHORIZED);
				return response.setComplete();
			}
		}
		return chain.filter(exchange);
	}

	public String getToken(String header, JWTokenHelper helper) {
		if (header != null && header.contains("Bearer")) {
			String token = header.split(" ")[1].trim();
			if (helper.isValid(token)) {
				return token;
			} else {
				return "";
			}
		} else {
			
			return "";
		}
	}

	public String getTokenFromQueryParam(ServerHttpRequest request, JWTokenHelper helper) {
		System.out.println("Has no header");
		if(request.getQueryParams().containsKey("Authorization")) {
			String token = request.getQueryParams().get("Authorization").get(0);
			if (helper.isValid(token)) {
				System.out.println("Is param!");
				return token;
			}
			else {
				return "";
			}
		}
		else {
			return "";
		}
			
	}
}