package com.tcd.ase.externaldata.controller;

import java.time.Duration;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcd.ase.externaldata.model.Data;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ExternalDataController {

	@GetMapping(value = "/json/flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<String> streamJsonObjects() {
		return Flux.interval(Duration.ofSeconds(20)).map(i -> Data.getTime());
	}

}
