package com.tcd.ase.externaldata.repository.bus;

import com.tcd.ase.externaldata.entity.bus.DublinBusStop;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DublinBusStopsRepository extends MongoRepository<DublinBusStop, String> {
}