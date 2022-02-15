package com.tcd.ase.realtimedataprocessor.service;

import com.tcd.ase.realtimedataprocessor.models.DataIndicatorEnum;
import com.tcd.ase.realtimedataprocessor.models.DublinBike;
import com.tcd.ase.realtimedataprocessor.producers.DublinBikesProducer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.xml.crypto.Data;

@Service
public class DublinBikeService {

    @Autowired
    DublinBikesProducer producer;

    private static final Logger log = LogManager.getLogger(DublinBikesProducer.class);

    @Scheduled(fixedRate = 60000)
    public void processRealTimeDataForDublinBikes() {
        DublinBike[] dublinBikes = getDublinBikeDataFromExternalSource();
        //saveDatatoDB();
        producer.sendMessage(DataIndicatorEnum.DUBLIN_BIKES.getTopic(), dublinBikes);
    }

    public DublinBike[] getDublinBikeDataFromExternalSource() {
        RestTemplate restTemplate = new RestTemplate();
        DublinBike[] dublinBikes = restTemplate.getForObject(DataIndicatorEnum.DUBLIN_BIKES.getEndpoint(), DublinBike[].class);
        log.info(dublinBikes.toString());
        return dublinBikes;
    }
}
