package com.tcd.ase.externaldata.repository.bus;

import com.tcd.ase.externaldata.entity.DublinBusHistorical;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DublinBusHistoricalRepository extends MongoRepository<DublinBusHistorical, String> {
}
