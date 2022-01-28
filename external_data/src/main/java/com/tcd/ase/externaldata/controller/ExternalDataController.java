package com.tcd.ase.externaldata.controller;

import java.time.Duration;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.tcd.ase.externaldata.model.Data1;

import reactor.core.publisher.Flux;

@RestController
public class ExternalDataController {

	@GetMapping(value = "/json/flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Long> streamJsonObjects() {
		return Flux.interval(Duration.ofSeconds(2)).map(i -> Data1.getTime());
	}

}

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
class Data {
	private final long seqNo;
	private final Instant timestamp;

	// ...
	public Data(long i, Instant timestamp) {
		this.seqNo = i;
		this.timestamp = timestamp;
	}

}
