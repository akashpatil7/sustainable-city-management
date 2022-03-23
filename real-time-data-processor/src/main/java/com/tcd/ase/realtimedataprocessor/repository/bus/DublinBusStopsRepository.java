package com.tcd.ase.realtimedataprocessor.repository.bus;

import com.tcd.ase.realtimedataprocessor.entity.DublinBusStops;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DublinBusStopsRepository extends MongoRepository<DublinBusStops, String> {
}