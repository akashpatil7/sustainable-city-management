package com.tcd.ase.realtimedataprocessor.repository.bus;

import com.tcd.ase.realtimedataprocessor.entity.DublinBusHistorical;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DublinBusHistoricalRepository extends MongoRepository<DublinBusHistorical, String> {
}
