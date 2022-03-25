package com.tcd.ase.realtimedataprocessor.repository.bus;

import com.tcd.ase.realtimedataprocessor.entity.DublinBusHistorical;
import org.springframework.data.mongodb.repository.MongoRepository;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.List;

public interface DublinBusHistoricalRepository extends MongoRepository<DublinBusHistorical, String> {

    Optional<DublinBusHistorical> findFirstByRouteIdAndTripIdAndStartTimestamp(String routeId, String tripId, Long startTimestamp);
    Optional<List<DublinBusHistorical>> findByStartTimestampGreaterThan(Long startTimestamp);

}
