package com.tcd.ase.realtimedataprocessor.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.tcd.ase.realtimedataprocessor.entity.AqiDAO;
import com.tcd.ase.realtimedataprocessor.entity.SimulatedData;
import com.tcd.ase.realtimedataprocessor.models.Aqi;
import com.tcd.ase.realtimedataprocessor.models.Aqis;
import com.tcd.ase.realtimedataprocessor.models.DataIndicatorEnum;
import com.tcd.ase.realtimedataprocessor.models.DublinAqiDataStation;
import com.tcd.ase.realtimedataprocessor.models.PedestrianCount;
import com.tcd.ase.realtimedataprocessor.models.SimulatedAqi;
import com.tcd.ase.realtimedataprocessor.producers.AqiProducer;
import com.tcd.ase.realtimedataprocessor.repository.AqiRepository;
import com.tcd.ase.realtimedataprocessor.repository.SimulationRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class AqiService {

    @Autowired
    AqiProducer producer;

    @Autowired
    AqiRepository aqiRepository;
    
    @Autowired
    EurekaClient eurekaClient;
    
    @Autowired
    SimulationRepository repository;
    
    @Value("${app.simulationservice.serviceId}")
    private String simulationService;

    @Scheduled(fixedRate = 3600000)
    public void processRealTimeDataForAqi() {
        log.info("[AQI] Processing");
        Aqi[] aqi;
        SimulatedData simulated = repository.findAll().get(0);
        if(simulated.isSimulated()) {
        	log.info("Staring simulation");
        	aqi = (Aqi[]) getDataFromSimulatedSource();
        }
        else {
        	aqi = getAqiDataFromExternalSource();
        	saveDataToDB(aqi);
        }
        producer.sendMessage(DataIndicatorEnum.AQI.getTopic(), aqi);
        
    }

    private Aqi[] getDataFromSimulatedSource() {
    	log.info("=== In simulation data generation method === ");
    	InstanceInfo instanceInfo = eurekaClient.getApplication(simulationService).getInstances().get(0);
    	String url = "http://" + instanceInfo.getIPAddr() + ":" + instanceInfo.getPort() + "/" + "models/aqi/getAqiPredictions";
    	log.info(url);
    	RestTemplate restTemplate = new RestTemplate();
    	Aqi[] aqilist = restTemplate.getForObject(url, Aqi[].class);
        return aqilist;
	}

	public Aqi[] getAqiDataFromExternalSource() {
        RestTemplate restTemplate = new RestTemplate();
        Aqis aqiData = restTemplate.getForObject(DataIndicatorEnum.AQI.getEndpoint(), Aqis.class);
        Aqi[] aqis = aqiData.getData();
        Aqi[] irishAqis = getIrishStations(aqis);
        return irishAqis;
    }

    public Aqi[] getIrishStations(Aqi[] stations) {
        if (stations == null) {
            return null;
        }
        Aqi[] irishStations = Arrays.stream(stations).filter(s -> s.getStation().getCountry().toString().contains("IE")).toArray(Aqi[]::new);
        return irishStations;
    }

    private void saveDataToDB(Aqi[] data) {
        log.info("[AQI] Updating Database");
        try {
            Long currentEpoch = data[0].getTime().getVTime();
            AqiDAO latestAqiFromDB = aqiRepository.findFirstByOrderByLastUpdatedTimeDesc().orElse(null);

            if (latestAqiFromDB == null || currentEpoch > latestAqiFromDB.getLastUpdatedTime()) {
                log.info("[AQI] New Data found");
                aqiRepository.saveAll(convertData(data));
            }
        } catch (Exception e) {
            log.error("[AQI] Error occurred while parsing response from aqi "+ e.getMessage());
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
