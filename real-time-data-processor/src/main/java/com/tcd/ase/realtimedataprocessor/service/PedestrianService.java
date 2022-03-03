package com.tcd.ase.realtimedataprocessor.service;

import com.tcd.ase.realtimedataprocessor.entity.PedestrianDAO;
import com.tcd.ase.realtimedataprocessor.entity.PedestrianInfoDAO;
import com.tcd.ase.realtimedataprocessor.models.DataIndicatorEnum;
import com.tcd.ase.realtimedataprocessor.models.Pedestrian;
import com.tcd.ase.realtimedataprocessor.models.PedestrianCount;
import com.tcd.ase.realtimedataprocessor.producers.PedestrianProducer;
import com.tcd.ase.realtimedataprocessor.repository.PedestrianInfoRepository;
import com.tcd.ase.realtimedataprocessor.repository.PedestrianRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;


@Service
public class PedestrianService {

    @Autowired
    PedestrianProducer producer;

    @Autowired
    PedestrianRepository pedestrianRepository;

    @Autowired
    PedestrianInfoRepository pedestrianInfoRepository;

    private static final Logger log = LogManager.getLogger(PedestrianProducer.class);

    public Resource loadEmployeesWithClassPathResource() {
        return new ClassPathResource("DublinStreetsLatLon.json");
    }

    @Scheduled(fixedRate = 60000)
    public void processRealTimeDataForPedestrian() {
        Pedestrian[] pedestrian = getPedestrianDataFromExternalSource();
        producer.sendMessage(DataIndicatorEnum.PEDESTRIAN.getTopic(), pedestrian);
        saveDataToDB(pedestrian);
    }

    private Pedestrian[] getPedestrianDataFromExternalSource() {
        RestTemplate restTemplate = new RestTemplate();
        JSONObject pedestrianBodyData = restTemplate.getForObject(DataIndicatorEnum.PEDESTRIAN.getEndpoint(),
                JSONObject.class);
        Pedestrian[] pedestrianData = formatPedestrianData(pedestrianBodyData);
        return pedestrianData;
    }

    private Pedestrian[] formatPedestrianData(Object pedestrianBodyData) {
        Pedestrian[] pedestrianData = null;
        JSONParser parser = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
        try {
            JSONObject json = (JSONObject) parser.parse(pedestrianBodyData.toString());
            JSONObject result = (JSONObject) json.get("result");
            JSONArray records = (JSONArray) result.get("records");
            pedestrianData = new Pedestrian[records.size()];
            for (int i = 0; i < records.size(); i++) {
                Pedestrian pedestrianObject = new Pedestrian();
                JSONObject obj = (JSONObject) records.get(i);
                pedestrianObject.setId((Long) obj.get("_id"));
                String time = (String) obj.get("Time");
                Long timestamp = convertDateToTimestamp(time);
                pedestrianObject.setTime(timestamp);
                pedestrianObject.setPedestrianCount(getPedestrianCounts(obj));
                pedestrianData[i] = pedestrianObject;
            }
            return pedestrianData;
        } catch (ParseException e) {
            e.printStackTrace();
            return pedestrianData;
        }
    }

    private PedestrianCount[] getPedestrianCounts(JSONObject obj) {
        PedestrianCount[] counts = null;
        try {
            List<PedestrianInfoDAO> streets = pedestrianInfoRepository.findAll();
            counts = new PedestrianCount[streets.size()];
            for (int i = 0; i < streets.size(); i++) {
                PedestrianCount count = new PedestrianCount();
                count.setStreet(streets.get(i).getStreetName());
                Long lat = Long.parseLong(streets.get(i).getStreetLatitude());
                Long lon = Long.parseLong(streets.get(i).getStreetLongitude());
                count.setStreetLatitude(lat);
                count.setStreetLongitude(lon);
                count.setCount((Long) obj.get(streets.get(i).getStreetName()));
                counts[i] = count;
            }
            return counts;
        } catch (Exception e) {
            e.printStackTrace();
            return counts;
        }
    }

    public static String readFileAsString(Path file)throws Exception
    {
        return new String(Files.readAllBytes(file));
    }

    private Long convertDateToTimestamp(String date) {
        LocalDateTime localDateTime = LocalDateTime.parse(date);
        Long timeInSeconds = localDateTime.toEpochSecond(ZoneOffset.UTC);
        return timeInSeconds;
    }

    private void saveDataToDB(Pedestrian[] data) {
        log.info("Comparing the data from the database");
        try {
            Long currentEpoch = data[0].getTime();
            PedestrianDAO latestPedestrianFromDB = pedestrianRepository.findFirstByOrderByTimeDesc().orElse(null);

            if (latestPedestrianFromDB == null || currentEpoch > latestPedestrianFromDB.getTime()) {
                log.info("New Data found");
                log.info(convertData(data));
                pedestrianRepository.saveAll(convertData(data));
            }
        } catch (Exception e) {
            log.error("Error occurred while parsing response from pedestrian " + e.getMessage());
        }
    }

    private ArrayList<PedestrianDAO> convertData(Pedestrian[] pedestrians) {

        ArrayList<PedestrianDAO> pedestrianList = new ArrayList<PedestrianDAO>();
        for (Pedestrian pedestrian : pedestrians) {
            PedestrianDAO pedestrianData = new PedestrianDAO.PedestrianBuilder().withId(pedestrian.getId())
                    .withPedestrianCount(pedestrian.getPedestrianCount())
                    .withTime(pedestrian.getTime()).build();
            pedestrianList.add(pedestrianData);
        }
        return pedestrianList;
    }

}
