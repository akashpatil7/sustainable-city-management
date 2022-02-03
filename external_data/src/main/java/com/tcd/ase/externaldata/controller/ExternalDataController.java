package com.tcd.ase.externaldata.controller;

import java.time.Duration;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController
public class ExternalDataController {
	@Autowired
    Flux<String> flux;
	
	
	@GetMapping(value = "/json/flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<String> streamJsonObjects() {
		return flux;
		//return Flux.interval(Duration.ofSeconds(2)).map(seq -> "Flux - " + LocalTime.now().toString());
	}

}
