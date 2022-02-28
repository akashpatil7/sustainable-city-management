package com.tcd.ase.realtimedataprocessor.service;

import com.tcd.ase.realtimedataprocessor.entity.AqiDAO;
import com.tcd.ase.realtimedataprocessor.models.DataIndicatorEnum;
import com.tcd.ase.realtimedataprocessor.models.Aqi;
import com.tcd.ase.realtimedataprocessor.models.Aqis;
import com.tcd.ase.realtimedataprocessor.producers.AqiProducer;
import com.tcd.ase.realtimedataprocessor.repository.AqiRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;

@Service
public class AqiService {

    @Autowired
    AqiProducer producer;

    @Autowired
    AqiRepository aqiRepository;

    private static final Logger log = LogManager.getLogger(AqiProducer.class);

    @Scheduled(fixedRate = 60000)
    public void processRealTimeDataForAqi() {
        Aqi[] aqi = getAqiDataFromExternalSource();
        producer.sendMessage(DataIndicatorEnum.AQI.getTopic(), aqi);
        saveDataToDB(aqi);
    }

    public Aqi[] getAqiDataFromExternalSource() {
        RestTemplate restTemplate = new RestTemplate();
        Aqis aqiData = restTemplate.getForObject(DataIndicatorEnum.AQI.getEndpoint(), Aqis.class);
        Aqi[] aqis = aqiData.getData();
        Aqi[] irishAqis = getIrishStations(aqis);
        log.info(irishAqis.toString());
        return irishAqis;
    }

    public Aqi[] getIrishStations(Aqi[] stations) {
        Aqi[] irishStations = new Aqi[stations.length];
        int index = 0;
        for(int i=0; i < stations.length; i++) {
            if(stations[i].getStation().getCountry().toString().contains("IE")) {
                irishStations[index] = stations[i];
                index++;
            }
        }
        return irishStations;
    }

    private void saveDataToDB(Aqi[] data) {
        log.info("Comparing the data from the database");
        try {
            Long currentEpoch = data[0].getTime().getVTime();
            AqiDAO latestAqiFromDB = aqiRepository.findFirstByOrderByLastUpdatedTimeDesc().orElse(null);

            if (latestAqiFromDB == null || currentEpoch > latestAqiFromDB.getLastUpdatedTime()) {
                log.info("New Data found");
                log.info(convertData(data));
                aqiRepository.saveAll(convertData(data));
            }
        } catch (Exception e) {
            log.error("Error occurred while parsing response from aqi "+ e.getMessage());
        }
    }

    private ArrayList<AqiDAO> convertData(Aqi[] aqis) {

        ArrayList<AqiDAO> aqiList = new ArrayList<AqiDAO>();
        for(Aqi aqi: aqis) {
            AqiDAO aqiData = new AqiDAO.AqiBuilder().withUid(aqi.getUid())
                    .withAqi(aqi.getAqi())
                    .withTime(aqi.getTime().getVTime())
                    .withStation(aqi.getStation().getName())
                    .withLatitude(aqi.getStation().getGeo()[0])
                    .withLongitude(aqi.getStation().getGeo()[1]).build();
            aqiList.add(aqiData);
        }
        return aqiList;
    }

}
