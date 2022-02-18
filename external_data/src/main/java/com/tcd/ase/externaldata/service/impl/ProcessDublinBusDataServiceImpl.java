package com.tcd.ase.externaldata.service.impl;

import com.tcd.ase.externaldata.entity.DublinBusStopSequence;
import com.tcd.ase.externaldata.model.DublinBus;
import com.tcd.ase.externaldata.model.dublinBus.Entity;
import com.tcd.ase.externaldata.model.dublinBus.Trip;
import com.tcd.ase.externaldata.repository.DublinBusStopSequenceRepository;
import com.tcd.ase.externaldata.repository.DublinCityBusRoutesRepository;
import com.tcd.ase.externaldata.service.ProcessDublinBusDataService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import java.util.stream.Collectors;

@Service
public class ProcessDublinBusDataServiceImpl implements ProcessDublinBusDataService {

    @Autowired
    private DublinCityBusRoutesRepository dublinCityBusRoutesRepository;

    @Autowired
    private DublinBusStopSequenceRepository dublinBusStopSequenceRepository;

    private static Logger LOGGER = LogManager.getLogger(ProcessDublinBusDataServiceImpl.class);

    @Override
    @Scheduled(fixedRate = 10000)
    public void processData() {
        Set<String> dublinCityBusRoutesList = dublinCityBusRoutesRepository.findAll().stream().map(i -> i.getRoute_id()).collect(Collectors.toSet());

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key","2de8ef2694a24e1c82fa92aaeba7d351");
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<DublinBus> dublinBusResponseEntity = restTemplate.exchange("https://api.nationaltransport.ie/gtfsr/v1?format=json", HttpMethod.GET, httpEntity, DublinBus.class, 1);

        List<Entity> entities = dublinBusResponseEntity.getBody().getEntity()
                                .stream()
                                .filter(x -> dublinCityBusRoutesList.contains(x.getTripUpdate().getTrip().getRouteId()))
                                .collect(Collectors.toList());
        saveToDB(entities);
        LOGGER.info("Processing completed for dublin bus response");
    }

    private void saveToDB(List<Entity> entities) {
        for (Entity busEntity: entities) {
            Trip trip = busEntity.getTripUpdate().getTrip();
            try {
                DublinBusStopSequence dublinBusStopSequence = new DublinBusStopSequence.DublinBusStopSequenceBuilder()
                                                                                    .withTripId(trip.getTripId())
                                                                                    .withRouteId(trip.getRouteId())
                                                                                    .withStartTimestamp(convertDateToTimestamp(trip.getStartDate().concat(trip.getStartTime()))) //please ignore this I'm still working on timestamp
                                                                                    .withScheduleRelationship(trip.getScheduleRelationship())
                                                                                    .withStopTimeUpdate(busEntity.getTripUpdate().getStopTimeUpdate())
                                                                                    .build();
                dublinBusStopSequenceRepository.save(dublinBusStopSequence);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }

    private Long convertDateToTimestamp(String startDateTime) throws ParseException {
        Date date=new SimpleDateFormat("yyyyMMddHH:mm:ss").parse(startDateTime);
        Long timeInSeconds = date.getTime();
        return timeInSeconds;
    }
}
