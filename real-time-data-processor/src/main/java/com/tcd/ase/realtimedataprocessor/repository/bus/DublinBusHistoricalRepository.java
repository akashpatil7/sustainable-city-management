package com.tcd.ase.realtimedataprocessor.repository.bus;

import com.tcd.ase.realtimedataprocessor.entity.DublinBusHistorical;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface DublinBusHistoricalRepository extends MongoRepository<DublinBusHistorical, String> {

    Optional<DublinBusHistorical> findFirstByRouteIdAndTripId(String routeId, String tripId);
    Optional<List<DublinBusHistorical>> findByStartTimestampGreaterThan(Long startTimestamp);

}
