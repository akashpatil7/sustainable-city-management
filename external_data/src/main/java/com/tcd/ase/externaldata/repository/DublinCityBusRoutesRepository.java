package com.tcd.ase.externaldata.repository;

import com.tcd.ase.externaldata.entity.DublinCityBusRoutes;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DublinCityBusRoutesRepository extends MongoRepository<DublinCityBusRoutes, String > {
}
