package com.tcd.ase.externaldata;

import com.tcd.ase.externaldata.client.DublinBikesClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ExternalDataApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExternalDataApplication.class, args);
	}
}
