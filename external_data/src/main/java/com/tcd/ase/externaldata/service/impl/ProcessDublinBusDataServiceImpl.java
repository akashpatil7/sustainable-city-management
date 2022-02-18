package com.tcd.ase.externaldata.service.impl;

import com.mongodb.client.model.Filters;
import com.tcd.ase.externaldata.entity.DublinBusHistorical;
import com.tcd.ase.externaldata.entity.DublinBusHistoricalStopSequence;
import com.tcd.ase.externaldata.entity.DublinBusStops;
import com.tcd.ase.externaldata.model.DublinBus;
import com.tcd.ase.externaldata.model.bus.Entity;
import com.tcd.ase.externaldata.model.bus.Trip;
import com.tcd.ase.externaldata.repository.bus.DublinBusHistoricalRepository;
import com.tcd.ase.externaldata.repository.bus.DublinBusStopsRepository;
import com.tcd.ase.externaldata.repository.bus.DublinBusRoutesRepository;
import com.tcd.ase.externaldata.service.ProcessDublinBusDataService;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProcessDublinBusDataServiceImpl implements ProcessDublinBusDataService {

    @Autowired
    private DublinBusRoutesRepository dublinBusRoutesRepository;

    @Autowired
    private DublinBusStopsRepository dublinBusStopsRepository;

    @Autowired
    private DublinBusHistoricalRepository dublinBusHistoricalRepository;

    @Override
    @Scheduled(fixedRate = 10000)
    public void processData() {

        Set<String> dublinCityBusRouteIdsList = dublinBusRoutesRepository.findAll()
                                                .stream()
                                                .map(i -> i.getRoute_id())
                                                .collect(Collectors.toSet());


        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key","2de8ef2694a24e1c82fa92aaeba7d351");
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<DublinBus> dublinBusResponseEntity =
                restTemplate.exchange("https://api.nationaltransport.ie/gtfsr/v1?format=json", HttpMethod.GET, httpEntity, DublinBus.class, 1);
        List<Entity> entities = dublinBusResponseEntity.getBody().getEntity()
                                .stream()
                                .filter(x -> dublinCityBusRouteIdsList
                                            .contains(x.getTripUpdate().getTrip().getRouteId()))
                                .collect(Collectors.toList());
        saveToDB(entities);
        System.out.println("Processing completed");
    }

    void saveToDB(List<Entity> entities) {

        List<DublinBusStops> DublinBusStopsList = dublinBusStopsRepository.findAll();

        for (Entity busEntity: entities) {
            Trip trip = busEntity.getTripUpdate().getTrip();
            DublinBusHistorical dublinBusHistorical = new DublinBusHistorical.DublinBusHistoricalBuilder()
                                                                                .withTripId(trip.getTripId())
                                                                                .withRouteId(trip.getRouteId())
                                                                                .withStartTimestamp(trip.getStartTime()) //please ignore this I'm still working on timestamp
                                                                                .withScheduleRelationship(trip.getScheduleRelationship())
                                                                                .withStopTimeUpdate(busEntity.getTripUpdate().getStopTimeUpdate())
                                                                                .build();
            Bson filter = Filters.and(Filters.eq("qty", 5), Filters.ne("color", "pink"));
            dublinBusHistoricalRepository.save(dublinBusHistorical);

        }
    }
}
