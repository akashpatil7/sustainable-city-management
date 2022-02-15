package com.tcd.ase.realtimedataprocessor.controller;

import com.tcd.ase.realtimedataprocessor.models.DataIndicatorEnum;
import com.tcd.ase.realtimedataprocessor.models.DublinBikeResponseDTO;
import com.tcd.ase.realtimedataprocessor.producers.DublinBikesProducer;
import com.tcd.ase.realtimedataprocessor.service.DublinBikeService;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
public class RealTimeDataProcessorController {

    @Autowired
    DublinBikeService service;

    @GetMapping(value = "/realTimeData/{dataIndicator}")
    public void sendDublinBusDataToKakfaTopic(@PathVariable(value = "dataIndicator") final String dataIndicator) {
        if(dataIndicator.equals("bike"))
            service.processRealTimeDataForDublinBikes();

    }

}
