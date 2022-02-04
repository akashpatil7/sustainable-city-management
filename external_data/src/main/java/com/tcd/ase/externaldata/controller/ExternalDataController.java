package com.tcd.ase.externaldata.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcd.ase.externaldata.model.DublinBikeResponseDTO;

import reactor.core.publisher.Flux;

@RestController
public class ExternalDataController {
	@Autowired
	Flux<DublinBikeResponseDTO> flux;

	@GetMapping(value = "/realTimeData", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<DublinBikeResponseDTO> streamRealTimeData() {
		return flux;
	}

}
