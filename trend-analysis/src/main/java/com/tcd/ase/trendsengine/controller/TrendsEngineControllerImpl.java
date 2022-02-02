package com.tcd.ase.trendsengine.controller;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

//import com.tcd.ase.trendsengine.repository.TrendsRepository;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@RestController
public class TrendsEngineControllerImpl implements TrendsEngineController {
	
	//@Autowired
	//private TrendsRepository repository;

	@Override
	public ResponseEntity<String> getTrendsData() {

		System.out.println("getTrendsData called!!");
		System.out.println("Setting up the client");
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		
		System.out.println("Setting up the request");
		Request request = new Request.Builder()
			.url("http://localhost:8050/getDailyAverages")
			.method("GET", null)
			.build();
		Response response;
		try {
			System.out.println("Trying to get response");
			response = client.newCall(request).execute();

			System.out.println("Returning response");
			return ResponseEntity.status(HttpStatus.OK).body(response.body().string());
		} catch (IOException e) {
			System.out.println("ERROR");
			e.printStackTrace();
		}
		System.out.println("FAILED");
		return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("fail");	
	}
}