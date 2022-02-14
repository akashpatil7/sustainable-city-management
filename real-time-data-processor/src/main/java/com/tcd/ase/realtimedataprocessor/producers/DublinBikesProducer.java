package com.tcd.ase.realtimedataprocessor.producers;

import com.tcd.ase.realtimedataprocessor.models.DublinBikeResponseDTO;
import com.tcd.ase.realtimedataprocessor.models.DublinBike;
import com.tcd.ase.realtimedataprocessor.models.DublinBikes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class DublinBikesProducer {

    private static final String topic = "dublinbike";

    private static final Logger log = LogManager.getLogger(DublinBikesProducer.class);

    @Value("${dublinBikesLatestDataURL}")
    private String DUBLIN_BUS_URI;

    @Autowired
    private KafkaTemplate<String,DublinBike[]> kakfaTemplate;

    @Scheduled(fixedRate = 60000)
    public void sendMessage() {
        DublinBike[] data = getDublinBikeDataFromExternalSource();
        this.kakfaTemplate.send(topic, data);
    }

    private DublinBike[] getDublinBikeDataFromExternalSource() {
        RestTemplate restTemplate = new RestTemplate();
        DublinBike[] dublinBikes = restTemplate.getForObject(DUBLIN_BUS_URI, DublinBike[].class);
        log.info(dublinBikes.toString());
        return dublinBikes;
    }
}