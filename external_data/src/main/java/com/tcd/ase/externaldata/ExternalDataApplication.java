package com.tcd.ase.externaldata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.tcd.ase.externaldata.model.Data;
import com.tcd.ase.externaldata.service.impl.ProcessDublinBikesDataServiceImpl;

@SpringBootApplication
@EnableScheduling
public class ExternalDataApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExternalDataApplication.class, args);
		Data.initialize();
	}
}
