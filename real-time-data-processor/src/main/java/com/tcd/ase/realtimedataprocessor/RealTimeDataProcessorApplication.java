package com.tcd.ase.realtimedataprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RealTimeDataProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(RealTimeDataProcessorApplication.class, args);
	}

}
