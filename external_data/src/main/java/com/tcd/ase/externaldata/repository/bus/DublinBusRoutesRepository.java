package com.tcd.ase.externaldata.repository.bus;

import com.tcd.ase.externaldata.entity.bus.DublinBusRoute;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DublinBusRoutesRepository extends MongoRepository<DublinBusRoute, String > {
}
