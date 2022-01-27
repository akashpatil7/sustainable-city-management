package com.tcd.ase.userservice.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationRequest {

	private String username;
	private String email;
	private String password;
}
