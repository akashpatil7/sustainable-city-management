package com.tcd.ase.realtimedataprocessor.service;

import com.tcd.ase.realtimedataprocessor.entity.DublinBusHistorical;
import com.tcd.ase.realtimedataprocessor.entity.DublinBusHistoricalStopSequence;
import com.tcd.ase.realtimedataprocessor.entity.DublinBusStops;
import com.tcd.ase.realtimedataprocessor.entity.DublinCityBusRoutes;
import com.tcd.ase.realtimedataprocessor.models.DataIndicatorEnum;
import com.tcd.ase.realtimedataprocessor.models.DublinBus;
import com.tcd.ase.realtimedataprocessor.models.bus.Entity;
import com.tcd.ase.realtimedataprocessor.models.bus.StopTimeUpdate;
import com.tcd.ase.realtimedataprocessor.models.bus.Trip;
import com.tcd.ase.realtimedataprocessor.producers.DublinBusProducer;
import com.tcd.ase.realtimedataprocessor.repository.bus.DublinBusHistoricalRepository;
import com.tcd.ase.realtimedataprocessor.repository.bus.DublinBusRoutesRepository;
import com.tcd.ase.realtimedataprocessor.repository.bus.DublinBusStopsRepository;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Autowired
    @Qualifier("dublinBus")
    private NewTopic dublinBusTopic;

    private List<DublinCityBusRoutes> dublinCityBusRoutes = new ArrayList<>();
    private List<DublinBusStops> dublinBusStopList = new ArrayList<>();

    private static Logger LOGGER = LogManager.getLogger(DublinBusService.class);

    @Scheduled(fixedRate = 60000)
    public void processRealTimeDataForDublinBikes() {
        List<DublinBusHistorical> dublinBusHistorical = getDublinBusDataFromExternalSource();
        dublinBusProducer.sendMessage(dublinBusTopic.name(), dublinBusHistorical);
        saveDataToDB(dublinBusHistorical);
    }

    public List<DublinBusHistorical> getDublinBusDataFromExternalSource() {
        dublinCityBusRoutes = dublinBusRoutesRepository.findAll();
        // get list of all bus stops
        dublinBusStopList = dublinBusStopsRepository.findAll();

        // fetch list of all dublin bus route ids
        Set<String> dublinBusRouteIdsList = dublinCityBusRoutes
                .stream()
                .map(DublinCityBusRoutes::getRoute_id)
                .collect(Collectors.toSet());

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key","2de8ef2694a24e1c82fa92aaeba7d351");
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<DublinBus> dublinBusResponseEntity =
                restTemplate.exchange(DataIndicatorEnum.DUBLIN_BUS.getEndpoint(), HttpMethod.GET, httpEntity, DublinBus.class, 1);

        // filter data for only DublinBus routes
        List<Entity> dublinBusEntities = filterByRoutes(dublinBusRouteIdsList, dublinBusResponseEntity);

        // build new entities with mapped data for stops and routes
        List<DublinBusHistorical> updatedDublinBusEntities = buildDublinBusEntities(dublinBusEntities);
        return updatedDublinBusEntities;
    }

    private List<Entity> filterByRoutes(Set<String> dublinBusRouteIdsList, ResponseEntity<DublinBus> dublinBusResponseEntity) {
        // filter API response data for dublin bus agency only
        return dublinBusResponseEntity.getBody().getEntity().stream()
                .filter(x -> dublinBusRouteIdsList.contains(x.getTripUpdate().getTrip().getRouteId()))
                .collect(Collectors.toList());
    }

    List<DublinBusHistorical> buildDublinBusEntities(List<Entity> dublinBusEntities) {

        List<DublinBusHistorical> updatedDublinBusEntities = new ArrayList<>();
        for (Entity busEntity: dublinBusEntities ) {
            Trip currentTrip = busEntity.getTripUpdate().getTrip();
            ArrayList<StopTimeUpdate> currentTripStopSequence = busEntity.getTripUpdate().getStopTimeUpdate();

            DublinCityBusRoutes currentDublinBusRoute = getDublinBusRouteById(currentTrip.getRouteId());
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

    private DublinCityBusRoutes getDublinBusRouteById(String routeId) {
        // return dublin bus route for routeId
        return dublinCityBusRoutes.stream()
                .filter(x -> x.getRoute_id().equals(routeId))
                .findFirst()
                .get();
    }

    private DublinBusStops getDublinBusStopById(String stopId) {
        // return bus stop for stopId
        return dublinBusStopList.stream()
                .filter(x -> x.getStop_id().equals(stopId))
                .findFirst()
                .get();
    }

    private void saveDataToDB(List<DublinBusHistorical> dublinBusEntities) {

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

    private ArrayList<DublinBusHistoricalStopSequence> getUpdatedStopSequence(ArrayList<StopTimeUpdate> currentTripStopSequence) {
        ArrayList<DublinBusHistoricalStopSequence> dublinBusHistoricalStopSequenceList = new ArrayList<>();

        for (StopTimeUpdate stopTimeUpdate: currentTripStopSequence) {
            DublinBusStops currentDublinBusStop = getDublinBusStopById(stopTimeUpdate.getStopId());

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

    private Long convertDateToTimestamp(String startDateTime) throws ParseException{
        Date date=new SimpleDateFormat("yyyyMMddHH:mm:ss").parse(startDateTime);
        Long timeInSeconds = date.getTime();
        return timeInSeconds;
    }

}
