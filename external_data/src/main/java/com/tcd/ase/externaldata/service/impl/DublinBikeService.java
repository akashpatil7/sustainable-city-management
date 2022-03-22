package com.tcd.ase.externaldata.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tcd.ase.externaldata.entity.DublinBikeDAO;
import com.tcd.ase.externaldata.model.DataIndicatorEnum;
import com.tcd.ase.externaldata.model.DublinBike;
import com.tcd.ase.externaldata.producers.DublinBikesProducer;
import com.tcd.ase.externaldata.repository.DublinBikesRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@EnableScheduling
public class DublinBikeService {

    @Autowired
    DublinBikesProducer producer;

    @Autowired
    DublinBikesRepository dublinBikesRepository;

    @Scheduled(fixedRate = 60000)
    public void processRealTimeDataForDublinBikes() {
        DublinBike[] dublinBikes = getDublinBikeDataFromExternalSource();
        producer.sendMessage(DataIndicatorEnum.DUBLIN_BIKES.getTopic(), dublinBikes);
        saveDataToDB(dublinBikes);
    }

    private DublinBike[] getDublinBikeDataFromExternalSource() {
        RestTemplate restTemplate = new RestTemplate();
        DublinBike[] dublinBikes = restTemplate.getForObject(DataIndicatorEnum.DUBLIN_BIKES.getEndpoint(), DublinBike[].class);
        log.info(dublinBikes.toString());
        return dublinBikes;
    }

    private void saveDataToDB(DublinBike[] data) {
        log.info("Comparing the data from the database");
        try {
            Long currentEpoch = convertDateToTimestamp(data[0].getHarvest_time());
            DublinBikeDAO latestBikeFromDB = dublinBikesRepository.findFirstByOrderByHarvestTimeDesc().orElse(null);

            if (latestBikeFromDB != null && currentEpoch > latestBikeFromDB.getHarvestTime()) {
                log.info("New Data found");
                dublinBikesRepository.saveAll(convertData(data));
                //this.sink.tryEmitNext(bikeResponseDTO);
            }
        } catch (Exception e) {
            log.error("Error occurred while parsing response from dublin bikes "+ e.getMessage());
        }
    }

    private Long convertDateToTimestamp(String date) {
        LocalDateTime localDateTime = LocalDateTime.parse(date);
        Long timeInSeconds = localDateTime.toEpochSecond(ZoneOffset.UTC);
        return timeInSeconds;
    }

    private ArrayList<DublinBikeDAO> convertData(DublinBike[] dublinBikes) {

        ArrayList<DublinBikeDAO> dublinBikesList = new ArrayList<DublinBikeDAO>();
        for(DublinBike dublinBike: dublinBikes) {
            DublinBikeDAO bikeData = new DublinBikeDAO.DublinBikesBuilder().withId(dublinBike.getId())
                    .withHarvestTime(convertDateToTimestamp(dublinBike.getHarvest_time()))
                    .withStationId(dublinBike.getStation_id())
                    .withAvailableBikeStands(dublinBike.getAvailable_bike_stands())
                    .withBikeStands(dublinBike.getBike_stands()).withAvailableBikes(dublinBike.getAvailable_bikes())
                    .withBanking(dublinBike.getBanking()).withBonus(dublinBike.getBonus())
                    .withLastUpdate(convertDateToTimestamp(dublinBike.getLast_update())).withStatus(dublinBike.getStatus())
                    .withAddress(dublinBike.getAddress()).withName(dublinBike.getName())
                    .withLatitude(dublinBike.getLatitude()).withLongitude(dublinBike.getLongitude()).build();
            dublinBikesList.add(bikeData);
        }
        return dublinBikesList;
    }

}