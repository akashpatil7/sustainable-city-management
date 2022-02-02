package com.tcd.ase.externaldata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.tcd.ase.externaldata.model.Data;

@SpringBootApplication
@EnableScheduling
@EnableKafkaStreams
public class ExternalDataApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExternalDataApplication.class, args);
		Data.initialize();
	}
}
