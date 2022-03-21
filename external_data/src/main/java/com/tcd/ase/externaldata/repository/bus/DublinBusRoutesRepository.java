package com.tcd.ase.externaldata.repository.bus;

import com.tcd.ase.externaldata.entity.DublinCityBusRoutes;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DublinBusRoutesRepository extends MongoRepository<DublinCityBusRoutes, String > {
}
