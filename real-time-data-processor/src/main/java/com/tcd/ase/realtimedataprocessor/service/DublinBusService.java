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

import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
public class DublinBusService {

    @Autowired
    DublinBusRoutesRepository dublinBusRoutesRepository;

    @Autowired
    DublinBusStopsRepository dublinBusStopsRepository;

    @Autowired
    DublinBusHistoricalRepository dublinBusHistoricalRepository;

    @Autowired
    DublinBusProducer dublinBusProducer;
    @Autowired
    @Qualifier("dublinBus")
    private NewTopic dublinBusTopic;

    RestTemplate restTemplate = new RestTemplate();

    private List<DublinCityBusRoutes> dublinCityBusRoutes = new ArrayList<>();
    private List<DublinBusStops> dublinBusStopList = new ArrayList<>();

    @Scheduled(fixedRate = 120000)
    public void processRealTimeDataForDublinBus() {
        log.info("[BUS] Processing");
        List<DublinBusHistorical> dublinBusHistorical = getDublinBusDataFromExternalSource();
        if (saveDataToDB(dublinBusHistorical)) {
            List<String> updatedList = Arrays.asList("Updated");
            dublinBusProducer.sendMessage(dublinBusTopic.name(), updatedList);
        }
    }

    public List<DublinBusHistorical> getDublinBusUpdate() {
        List<DublinBusHistorical> updatedBusList = new ArrayList<>();
        // 72,00,000 denotes 2 hours
        Long now = System.currentTimeMillis() - 7200000;
        Optional<List<DublinBusHistorical>> updatedBus = dublinBusHistoricalRepository.findByStartTimestampGreaterThan(now);
        if (updatedBus.isPresent()) {
            log.info("[BUS] Fetching data");
            updatedBusList = (List<DublinBusHistorical>) updatedBus.get();
        }
        return updatedBusList;
    }

    @PostConstruct
    private void loadRoutesAndStops() {
        // get list of all bus routes
        dublinCityBusRoutes = dublinBusRoutesRepository.findAll();

        // get list of all bus stops
        dublinBusStopList = dublinBusStopsRepository.findAll();
    }

    public List<DublinBusHistorical> getDublinBusDataFromExternalSource() {
        // fetch list of all dublin bus route ids
        Set<String> dublinBusRouteIdsList = dublinCityBusRoutes
                .stream()
                .map(DublinCityBusRoutes::getRoute_id)
                .collect(Collectors.toSet());

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key","2de8ef2694a24e1c82fa92aaeba7d351");
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);

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
        if (dublinBusResponseEntity == null) {
            return null;
        }
        else {
            return dublinBusResponseEntity.getBody().getEntity().stream()
            .filter(x -> dublinBusRouteIdsList.contains(x.getTripUpdate().getTrip().getRouteId()))
            .collect(Collectors.toList());
        }
    }

    List<DublinBusHistorical> buildDublinBusEntities(List<Entity> dublinBusEntities) {

        List<DublinBusHistorical> updatedDublinBusEntities = new ArrayList<>();
        if (dublinBusEntities == null) {
            return updatedDublinBusEntities;
        }
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

    private boolean saveDataToDB(List<DublinBusHistorical> dublinBusEntities) {
        log.info("[BUS] Updating database");
        boolean hasUpdates = false;
        for (DublinBusHistorical dublinBus: dublinBusEntities) {

            DublinBusHistorical dublinBusHistoricalFromDB =
                    dublinBusHistoricalRepository.findFirstByRouteIdAndTripId(
                            dublinBus.getRouteId(),
                            dublinBus.getTripId())
                            .orElse(null);

            if (dublinBusHistoricalFromDB != null && dublinBus.getStopSequence().size() > dublinBusHistoricalFromDB.getStopSequence().size())
            {
                hasUpdates = true;
                dublinBus.set_creationDate(dublinBusHistoricalFromDB.get_creationDate());
                dublinBus.set_lastModifiedDate(new Date().toString());
            }

            dublinBusHistoricalRepository.save(dublinBus);
        }
        log.info("[BUS] Has updates: " + hasUpdates);
        return hasUpdates;
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
