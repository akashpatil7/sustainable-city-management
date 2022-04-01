package com.tcd.ase.realtimedataprocessor.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;

@Document("database")
@Getter
public class SimulatedData {
	boolean simulated;
}
