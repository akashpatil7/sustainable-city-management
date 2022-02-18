package com.tcd.ase.externaldata.service.impl;

import com.mongodb.client.model.Filters;
import com.tcd.ase.externaldata.entity.DublinCityBusRoutes;
import com.tcd.ase.externaldata.model.DublinBus;
import com.tcd.ase.externaldata.service.ProcessDublinBusDataService;
import com.tcd.ase.externaldata.entity.DublinBusHistorical;
import com.tcd.ase.externaldata.entity.DublinBusStops;
import com.tcd.ase.externaldata.model.bus.Entity;
import com.tcd.ase.externaldata.model.bus.Trip;
import com.tcd.ase.externaldata.repository.bus.DublinBusHistoricalRepository;
import com.tcd.ase.externaldata.repository.bus.DublinBusStopsRepository;
import com.tcd.ase.externaldata.repository.bus.DublinBusRoutesRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    private static Logger LOGGER = LogManager.getLogger(ProcessDublinBusDataServiceImpl.class);

    @Override
    @Scheduled(fixedRate = 10000)
    public void processData() {

        List<DublinCityBusRoutes> dublinCityBusRoutes = dublinBusRoutesRepository.findAll();


        Set<String> dublinCityBusRouteIdsList = dublinCityBusRoutes
                .stream()
                .map(i -> i.getRoute_id())
                .collect(Collectors.toSet());

        Map<String, String> map = dublinCityBusRoutes
                .stream()
                .collect(Collectors.toMap(DublinCityBusRoutes::getRoute_id, DublinCityBusRoutes::getRoute_long_name));


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
        saveToDB(entities, map);
        LOGGER.info("Processing completed for dublin bus response");
    }


    void saveToDB(List<Entity> entities, Map<String, String> map) {

        List<DublinBusStops> DublinBusStopsList = dublinBusStopsRepository.findAll();

        for (Entity busEntity: entities) {
            Trip trip = busEntity.getTripUpdate().getTrip();
            DublinBusHistorical dublinBusHistorical = null;
            try {
                dublinBusHistorical = new DublinBusHistorical.DublinBusHistoricalBuilder()
                                                                                    .withTripId(trip.getTripId())
                                                                                    .withRouteId(trip.getRouteId())
                                                                                    .withStartTimestamp(convertDateToTimestamp(trip.getStartDate().concat(trip.getStartTime()))) //please ignore this I'm still working on timestamp
                                                                                    .withScheduleRelationship(trip.getScheduleRelationship())
                                                                                    .withStopTimeUpdate(busEntity.getTripUpdate().getStopTimeUpdate())
                                                                                    .build();
            } catch (ParseException e) {
                LOGGER.error("Error occurred while parsing a date : " + e.getMessage());
            }
            Bson filter = Filters.and(Filters.eq("qty", 5), Filters.ne("color", "pink"));
            dublinBusHistoricalRepository.save(dublinBusHistorical);
        }
    }

    private Long convertDateToTimestamp(String startDateTime) throws ParseException {
        Date date=new SimpleDateFormat("yyyyMMddHH:mm:ss").parse(startDateTime);
        Long timeInSeconds = date.getTime();
        return timeInSeconds;
    }
}
