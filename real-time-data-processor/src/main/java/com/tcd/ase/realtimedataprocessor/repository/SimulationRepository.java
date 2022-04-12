package com.tcd.ase.realtimedataprocessor.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tcd.ase.realtimedataprocessor.entity.SimulatedData;

public interface SimulationRepository extends MongoRepository<SimulatedData,String> {

}
