package com.tcd.ase.realtimedataprocessor.repository.bus;

import com.tcd.ase.realtimedataprocessor.entity.DublinCityBusRoutes;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DublinBusRoutesRepository extends MongoRepository<DublinCityBusRoutes, String > {
}
