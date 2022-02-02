package com.tcd.ase.externaldata.controller;

import java.time.Duration;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.codehaus.jettison.json.JSONArray;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcd.ase.externaldata.model.Data;

import reactor.core.publisher.Flux;

@RestController
public class ExternalDataController {

	@GetMapping(value = "/json/flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Long> streamJsonObjects() {
		return Flux.interval(Duration.ofSeconds(2)).map(i -> Data.getTime());
	}

	@GetMapping(value = "/getData/", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getWordCount() {
		KafkaStreams kafkaStreams = factoryBean.getKafkaStreams();
		ReadOnlyKeyValueStore<String, JSONArray> data = kafkaStreams.store(
				StoreQueryParameters.fromNameAndType("data", QueryableStoreTypes.keyValueStore())
		);
		return "Success";
	}
}
