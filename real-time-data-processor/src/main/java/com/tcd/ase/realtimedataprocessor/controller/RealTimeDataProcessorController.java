package com.tcd.ase.realtimedataprocessor.controller;

import com.tcd.ase.realtimedataprocessor.models.DublinBikeResponseDTO;
import com.tcd.ase.realtimedataprocessor.producers.DublinBikesProducer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class RealTimeDataProcessorController {

    @Autowired
    DublinBikesProducer producer;

    @GetMapping(value = "/realTimeData")
    public void sendDublinBusDataToKakfaTopic() {
        this.producer.getDublinBikeDataFromExternalSource();
    }

}
