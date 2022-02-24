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
        log.info(aqis.toString());
        return aqis;
    }

    public void saveDataToDB(Aqi[] data) {
        log.info("Comparing the data from the database");
        try {
            // TODO do epoch timing thing for aqi
            //Long currentEpoch = convertDateToTimestamp(data[0].getAqi());
            //AqiDAO latestAqiFromDB = null;

            //if (latestAqiFromDB != null /* TODO do epoch timing thing for aqi*/) {
                log.info("New Data found");
                log.info(convertData(data));
                aqiRepository.saveAll(convertData(data));
            //}
        } catch (Exception e) {
            log.error("Error occurred while parsing response from aqi "+ e.getMessage());
        }
    }

    private ArrayList<AqiDAO> convertData(Aqi[] aqis) {

        ArrayList<AqiDAO> aqiList = new ArrayList<AqiDAO>();
        for(Aqi aqi: aqis) {
            AqiDAO aqiData = new AqiDAO.AqiBuilder().withId(aqi.getUid())
                    .withAqi(aqi.getAqi()).build();
            aqiList.add(aqiData);
        }
        return aqiList;
    }

}
