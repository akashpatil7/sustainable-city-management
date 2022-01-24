package com.tcd.ase.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationGatewayFilter extends AbstractGatewayFilterFactory<Object> {

	@Autowired
	AuthenticationFilter filter;
	@Override
	public GatewayFilter apply(Object config) {
		return filter;
	}
	

}
