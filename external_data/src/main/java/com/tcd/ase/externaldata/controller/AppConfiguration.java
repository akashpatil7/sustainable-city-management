package com.tcd.ase.externaldata.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tcd.ase.externaldata.model.DublinBikeResponseDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Configuration
public class AppConfiguration {

	@Bean
	public Sinks.Many<DublinBikeResponseDTO> sink() {
		return Sinks.many().replay().latest();
	}

	@Bean
	public Flux<DublinBikeResponseDTO> flux(Sinks.Many<DublinBikeResponseDTO> sink) {
		return sink.asFlux().cache();
	}

}