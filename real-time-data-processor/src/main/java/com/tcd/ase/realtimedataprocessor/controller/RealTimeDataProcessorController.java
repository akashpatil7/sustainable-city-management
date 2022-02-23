package com.tcd.ase.realtimedataprocessor.controller;

import com.tcd.ase.realtimedataprocessor.models.Aqi;
import com.tcd.ase.realtimedataprocessor.models.DataIndicatorEnum;
import com.tcd.ase.realtimedataprocessor.models.DublinBike;
import com.tcd.ase.realtimedataprocessor.models.DublinBikeResponseDTO;
import com.tcd.ase.realtimedataprocessor.producers.DublinBikesProducer;
import com.tcd.ase.realtimedataprocessor.service.AqiService;
import com.tcd.ase.realtimedataprocessor.service.DublinBikeService;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class RealTimeDataProcessorController {

    @Autowired
    DublinBikeService service;

    @Autowired
    Flux<DublinBike[]> flux;

    @Autowired
    AqiService aqiService;

    @Autowired
    Flux<Aqi[]> aqiFlux;

    @GetMapping(value = "/realTimeData/{dataIndicator}")
    public void sendDataToKakfaTopic(@PathVariable(value = "dataIndicator") final String dataIndicator) {
        if(dataIndicator.equals("bike"))
            service.processRealTimeDataForDublinBikes();
        if(dataIndicator.equals("aqi"))
            aqiService.processRealTimeDataForAqi();
    }

    @GetMapping(value = "/getRealTimeData/{dataIndicator}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<DublinBike[]> streamRealTimeData(@PathVariable(value = "dataIndicator") final String dataIndicator) {
        return flux;
    }

    @GetMapping(value = "/getRealTimeData/{dataIndicator}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Aqi[]> streamRealTimeAqiData(@PathVariable(value = "dataIndicator") final String dataIndicator) {
        return aqiFlux;
    }

}
