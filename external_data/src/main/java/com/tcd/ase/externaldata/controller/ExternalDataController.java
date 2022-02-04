package com.tcd.ase.externaldata.controller;

import java.time.Duration;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcd.ase.externaldata.model.Data;

import reactor.core.publisher.Flux;

@RestController
public class ExternalDataController {

	@GetMapping(value = "/realTimeData", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<String> getRealTimeData() {
		return Flux.interval(Duration.ofSeconds(0), Duration.ofSeconds(20)).map(i -> Data.getTime());
	}

}
