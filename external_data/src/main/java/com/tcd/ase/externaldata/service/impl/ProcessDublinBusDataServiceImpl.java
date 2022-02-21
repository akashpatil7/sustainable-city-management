package com.tcd.ase.externaldata.service.impl;

import com.tcd.ase.externaldata.entity.DublinBusHistorical;
import com.tcd.ase.externaldata.entity.DublinBusHistoricalStopSequence;
import com.tcd.ase.externaldata.entity.bus.DublinBusRoute;
import com.tcd.ase.externaldata.entity.bus.DublinBusStop;
import com.tcd.ase.externaldata.model.DBus;
import com.tcd.ase.externaldata.model.bus.DublinBusEntity;
import com.tcd.ase.externaldata.model.bus.StopTimeUpdate;
import com.tcd.ase.externaldata.model.bus.Trip;
import com.tcd.ase.externaldata.repository.bus.DublinBusHistoricalRepository;
import com.tcd.ase.externaldata.repository.bus.DublinBusRoutesRepository;
import com.tcd.ase.externaldata.repository.bus.DublinBusStopsRepository;
import com.tcd.ase.externaldata.service.ProcessDublinBusDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProcessDublinBusDataServiceImpl implements ProcessDublinBusDataService {

    @Autowired
    private DublinBusRoutesRepository dublinBusRoutesRepository;

    @Autowired
    private DublinBusStopsRepository dublinBusStopsRepository;

    @Autowired
    private DublinBusHistoricalRepository dublinBusHistoricalRepository;

    private List<DublinBusRoute> dublinBusRouteList;
    private List<DublinBusStop> dublinBusStopList;

    private static Logger LOGGER = LogManager.getLogger(ProcessDublinBusDataServiceImpl.class);

    @Override
    @Scheduled(fixedRate = 50000)
    public void processData() {

        // fetch all dublin bus routes
        dublinBusRouteList = dublinBusRoutesRepository.findAll();

        // fetch list of all dublin bus route ids
        Set<String> dublinBusRouteIdsList = dublinBusRouteList
                .stream()
                .map(DublinBusRoute::getRoute_id)
                .collect(Collectors.toSet());

        // get list of all bus stops
        dublinBusStopList = dublinBusStopsRepository.findAll();

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key","2de8ef2694a24e1c82fa92aaeba7d351");
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<DBus> busResponseEntity =
                restTemplate.exchange(
                        "https://api.nationaltransport.ie/gtfsr/v1?format=json",
                        HttpMethod.GET,
                        httpEntity,
                        DBus.class,
                        1
                );

        // filter data for only DublinBus routes
        List<DublinBusEntity> dublinBusEntities = filterByRoutes(dublinBusRouteIdsList, busResponseEntity);

        // build new entities with mapped data for stops and routes
        List<DublinBusHistorical> updatedDublinBusEntities = buildDublinBusEntities(dublinBusEntities);

        saveToDB(updatedDublinBusEntities);
        LOGGER.info("Processing completed for dublin bus response");
    }

    List<DublinBusHistorical> buildDublinBusEntities(List<DublinBusEntity> dublinBusEntities) {

        List<DublinBusHistorical> updatedDublinBusEntities = new ArrayList<>();
        for (DublinBusEntity busEntity: dublinBusEntities ) {
            Trip currentTrip = busEntity.getTripUpdate().getTrip();
            ArrayList<StopTimeUpdate> currentTripStopSequence = busEntity.getTripUpdate().getStopTimeUpdate();

            DublinBusRoute currentDublinBusRoute = getDublinBusRouteById(currentTrip.getRouteId());
            ArrayList<DublinBusHistoricalStopSequence> updatedStopSequence =
                    Objects.nonNull(currentTripStopSequence) ? getUpdatedStopSequence(currentTripStopSequence) : new ArrayList<>();

            try {
                DublinBusHistorical updatedDublinBusEntity = new DublinBusHistorical.DublinBusHistoricalBuilder()
                        .withTripId(currentTrip.getTripId())
                        .withRouteId(currentTrip.getRouteId())
                        .withRouteShort(currentDublinBusRoute.getRoute_short_name())
                        .withRouteLong(currentDublinBusRoute.getRoute_long_name())
                        .withStartTimestamp(convertDateToTimestamp(currentTrip.getStartDate().concat(currentTrip.getStartTime())))
                        .withScheduleRelationship(currentTrip.getScheduleRelationship())
                        .withStopSequence(updatedStopSequence)
                        .with_CreationDate()
                        .build();

                updatedDublinBusEntities.add(updatedDublinBusEntity);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return updatedDublinBusEntities;
    }

    List<DublinBusEntity> filterByRoutes(Set<String> dublinBusRouteIdsList, ResponseEntity<DBus> dublinBusResponseEntity) {
        // filter API response data for dublin bus agency only
        return Arrays.stream(Objects.requireNonNull(dublinBusResponseEntity.getBody()).getEntity())
                .filter(x -> dublinBusRouteIdsList.contains(x.getTripUpdate().getTrip().getRouteId()))
                .collect(Collectors.toList());
    }

    DublinBusRoute getDublinBusRouteById(String routeId) {
        // return dublin bus route for routeId
        return dublinBusRouteList.stream()
                        .filter(x -> x.getRoute_id().equals(routeId))
                        .findFirst()
                        .get();
    }

    DublinBusStop getDublinBusStopById(String stopId) {
        // return bus stop for stopId
        return dublinBusStopList.stream()
                .filter(x -> x.getStop_id().equals(stopId))
                .findFirst()
                .get();
    }

    ArrayList<DublinBusHistoricalStopSequence> getUpdatedStopSequence(ArrayList<StopTimeUpdate> currentTripStopSequence) {
        ArrayList<DublinBusHistoricalStopSequence> dublinBusHistoricalStopSequenceList = new ArrayList<>();

        for (StopTimeUpdate stopTimeUpdate: currentTripStopSequence) {
            DublinBusStop currentDublinBusStop = getDublinBusStopById(stopTimeUpdate.getStopId());

            DublinBusHistoricalStopSequence dublinBusHistoricalStopSequence =
                    new DublinBusHistoricalStopSequence.DublinBusHistoricalStopSequenceBuilder()
                            .withStopSequence(stopTimeUpdate.getStopSequence())
                            .withStopId(stopTimeUpdate.getStopId())
                            .withArrivalDelay(Objects.nonNull(stopTimeUpdate.getArrival()) ? stopTimeUpdate.getArrival().getDelay() : -1)
                            .withDepartureDelay(Objects.nonNull(stopTimeUpdate.getDeparture()) ? stopTimeUpdate.getDeparture().getDelay() : -1)
                            .withStopName(currentDublinBusStop.getStop_name())
                            .withStopLat(currentDublinBusStop.getStop_lat())
                            .withStopLon(currentDublinBusStop.getStop_lon())
                            .withScheduleRelationship(stopTimeUpdate.getScheduleRelationship())
                            .build();

            dublinBusHistoricalStopSequenceList.add(dublinBusHistoricalStopSequence);
        }

        return dublinBusHistoricalStopSequenceList;
    }

    void saveToDB(List<DublinBusHistorical> dublinBusEntities) {

        for (DublinBusHistorical dublinBus: dublinBusEntities) {
            
            DublinBusHistorical dublinBusHistoricalFromDB =
                    dublinBusHistoricalRepository.findFirstByRouteIdAndTripId(
                                    dublinBus.getRouteId(),
                                    dublinBus.getTripId())
                            .orElse(null);

            if (dublinBusHistoricalFromDB != null)
                dublinBus.set_creationDate(dublinBusHistoricalFromDB.get_creationDate());

            dublinBusHistoricalRepository.save(dublinBus);
        }
    }

    private String convertDateToTimestamp(String startDateTime) throws ParseException {
        Date date = new SimpleDateFormat("yyyyMMddHH:mm:ss").parse(startDateTime);
        return date.toString();
    }
}
