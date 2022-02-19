package com.tcd.ase.realtimedataprocessor.service;

import com.mongodb.client.model.Filters;
import com.tcd.ase.realtimedataprocessor.entity.DublinBusHistorical;
import com.tcd.ase.realtimedataprocessor.entity.DublinBusStops;
import com.tcd.ase.realtimedataprocessor.entity.DublinCityBusRoutes;
import com.tcd.ase.realtimedataprocessor.models.DataIndicatorEnum;
import com.tcd.ase.realtimedataprocessor.models.DublinBus;
import com.tcd.ase.realtimedataprocessor.models.bus.Entity;
import com.tcd.ase.realtimedataprocessor.models.bus.Trip;
import com.tcd.ase.realtimedataprocessor.producers.DublinBusProducer;
import com.tcd.ase.realtimedataprocessor.repository.bus.DublinBusHistoricalRepository;
import com.tcd.ase.realtimedataprocessor.repository.bus.DublinBusRoutesRepository;
import com.tcd.ase.realtimedataprocessor.repository.bus.DublinBusStopsRepository;

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
import java.util.stream.Collectors;

@Service
public class DublinBusService {

    @Autowired
    private DublinBusRoutesRepository dublinBusRoutesRepository;

    @Autowired
    private DublinBusStopsRepository dublinBusStopsRepository;

    @Autowired
    private DublinBusHistoricalRepository dublinBusHistoricalRepository;

    @Autowired
    private DublinBusProducer dublinBusProducer;

    private static Logger LOGGER = LogManager.getLogger(DublinBusService.class);

    @Scheduled(fixedRate = 60000)
    public void processRealTimeDataForDublinBikes() {
        List<DublinBusHistorical> dublinBusHistorical = getDublinBusDataFromExternalSource();
        dublinBusProducer.sendMessage(DataIndicatorEnum.DUBLIN_BUS.getTopic(), dublinBusHistorical);
    }

    public List<DublinBusHistorical> getDublinBusDataFromExternalSource() {
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
                restTemplate.exchange(DataIndicatorEnum.DUBLIN_BUS.getEndpoint(), HttpMethod.GET, httpEntity, DublinBus.class, 1);

        List<Entity> entities = dublinBusResponseEntity.getBody().getEntity()
                .stream()
                .filter(x -> dublinCityBusRouteIdsList
                        .contains(x.getTripUpdate().getTrip().getRouteId()))
                .collect(Collectors.toList());

        return saveDataToDB(entities, map);
    }

    private List<DublinBusHistorical> saveDataToDB(List<Entity> entities, Map<String, String> map) {

        List<DublinBusHistorical> dublinBusHistorical = new ArrayList<>();

        List<DublinBusStops> DublinBusStopsList = dublinBusStopsRepository.findAll();

        for (Entity busEntity: entities) {
            Trip trip = busEntity.getTripUpdate().getTrip();
            try {
                DublinBusHistorical dublinBus = new DublinBusHistorical.DublinBusHistoricalBuilder()
                        .withTripId(trip.getTripId())
                        .withRouteId(trip.getRouteId())
                        .withStartTimestamp(convertDateToTimestamp(trip.getStartDate().concat(trip.getStartTime()))) //please ignore this I'm still working on timestamp
                        .withScheduleRelationship(trip.getScheduleRelationship())
                        .withStopTimeUpdate(busEntity.getTripUpdate().getStopTimeUpdate())
                        .build();

                dublinBusHistoricalRepository.save(dublinBus);
                dublinBusHistorical.add(dublinBus);

            } catch (ParseException e) {
                LOGGER.error("Error occurred while parsing a date : " + e.getMessage());
            }
            Bson filter = Filters.and(Filters.eq("qty", 5), Filters.ne("color", "pink"));
        }
        return dublinBusHistorical;
    }

    private Long convertDateToTimestamp(String startDateTime) throws ParseException{
        Date date=new SimpleDateFormat("yyyyMMddHH:mm:ss").parse(startDateTime);
        Long timeInSeconds = date.getTime();
        return timeInSeconds;
    }

}
