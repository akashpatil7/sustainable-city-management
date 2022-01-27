package com.tcd.ase.trendsengine.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.tcd.ase.trendsengine.models.TrendsRequest;

public interface TrendsEngineController {

	String BASE_URL = "/trends";

	@PostMapping(path = BASE_URL + "/getTrendsData")
	public ResponseEntity<Void> getTrendsData(@RequestBody TrendsRequest request);

	//@PostMapping(path = BASE_URL + "/login")
	//public ResponseEntity<String> login(@RequestBody TrendsRequest request);

}
