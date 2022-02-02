package com.tcd.ase.trendsengine.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.tcd.ase.trendsengine.models.TrendsRequest;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public interface TrendsEngineController {

	String BASE_URL = "/trends";

	@GetMapping(path = BASE_URL + "/getTrendsData")
	public ResponseEntity<String> getTrendsData();

	//@PostMapping(path = BASE_URL + "/login")
	//public ResponseEntity<String> login(@RequestBody TrendsRequest request);

}