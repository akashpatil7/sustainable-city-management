package com.tcd.ase.trendsengine.entity;

import lombok.Getter;
import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Users")
@Getter
@Setter
public class Trends {
	private String userName;
	@Id
	private String userEmail;
	private String password;
}
