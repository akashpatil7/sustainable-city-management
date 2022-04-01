package com.tcd.ase.realtimedataprocessor.service;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.tcd.ase.realtimedataprocessor.entity.DublinBikeDAO;
import com.tcd.ase.realtimedataprocessor.entity.SimulatedData;
import com.tcd.ase.realtimedataprocessor.models.DataIndicatorEnum;
import com.tcd.ase.realtimedataprocessor.models.DublinBike;
import com.tcd.ase.realtimedataprocessor.producers.DublinBikesProducer;
import com.tcd.ase.realtimedataprocessor.repository.DublinBikesRepository;
import com.tcd.ase.realtimedataprocessor.repository.SimulationRepository;

import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

@Service
@Log4j2
public class DublinBikeService {

    @Autowired
    DublinBikesProducer producer;

    @Autowired
    DublinBikesRepository dublinBikesRepository;
    
    @Autowired
    SimulationRepository repository;
    
    @Autowired
    EurekaClient eurekaClient;
    
    @Value("${app.simulationservice.serviceId}")
    private String simulationService;

    @Scheduled(fixedRate = 60000)
    public void processRealTimeDataForDublinBikes() {
        log.info("[BIKE] Processing");
        SimulatedData simulated = repository.findAll().get(0);
        DublinBike[] dublinBikes;
        if(simulated.isSimulated()) {
        	log.info("Staring simulation");
        	dublinBikes = getDublinBikeDataFromSimulatedSource();
        }
        else {
        	log.info("Staring actual data");
        	dublinBikes = getDublinBikeDataFromExternalSource();
        }
        producer.sendMessage(DataIndicatorEnum.DUBLIN_BIKES.getTopic(), dublinBikes);
        saveDataToDB(dublinBikes);
    }

    private DublinBike[] getDublinBikeDataFromSimulatedSource() {
    	log.info("=== In simulation data generation method === ");
    	InstanceInfo instanceInfo = eurekaClient.getApplication(simulationService).getInstances().get(0);
    	String url = "http://" + instanceInfo.getIPAddr() + ":" + instanceInfo.getPort() + "/" + "models/bikes/get_bikes_predictions";
    	log.info(url);
    	RestTemplate restTemplate = new RestTemplate();
        DublinBike[] dublinBikes = restTemplate.getForObject(DataIndicatorEnum.DUBLIN_BIKES.getEndpoint(), DublinBike[].class);
        log.info(dublinBikes.toString());
        return dublinBikes;
	}

	private DublinBike[] getDublinBikeDataFromExternalSource() {
        RestTemplate restTemplate = new RestTemplate();
        DublinBike[] dublinBikes = restTemplate.getForObject(DataIndicatorEnum.DUBLIN_BIKES.getEndpoint(), DublinBike[].class);
        log.info(dublinBikes.toString());
        return dublinBikes;
    }

    private void saveDataToDB(DublinBike[] data) {
        log.info("[BIKE] Updating database");
        try {
            Long currentEpoch = convertDateToTimestamp(data[0].getHarvest_time());
            DublinBikeDAO latestBikeFromDB = dublinBikesRepository.findFirstByOrderByHarvestTimeDesc().orElse(null);

            if (latestBikeFromDB != null && currentEpoch > latestBikeFromDB.getHarvestTime()) {
                log.info("[BIKE] New Data found");
                dublinBikesRepository.saveAll(convertData(data));
                //this.sink.tryEmitNext(bikeResponseDTO);
            }
        } catch (Exception e) {
            log.error("[BIKE] Error occurred while parsing response from dublin bikes "+ e.getMessage());
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
