package com.tcd.ase.externaldata.repository.bus;

import com.tcd.ase.externaldata.entity.DublinBusHistorical;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface DublinBusHistoricalRepository extends MongoRepository<DublinBusHistorical, String> {

    Optional<DublinBusHistorical> findFirstByRouteIdAndTripId(String routeId, String tripId);

}
