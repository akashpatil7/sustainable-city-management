package com.tcd.ase.realtimedataprocessor.controller;

import com.tcd.ase.realtimedataprocessor.entity.DublinBusHistorical;
import com.tcd.ase.realtimedataprocessor.models.DublinBike;
import com.tcd.ase.realtimedataprocessor.service.DublinBikeService;
import com.tcd.ase.realtimedataprocessor.service.DublinBusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class RealTimeDataProcessorController {

    @Autowired
    DublinBikeService service;

    @Autowired
    DublinBusService dublinBusService;

    @Autowired
    Flux<DublinBike[]> flux;

    @Autowired
    @Qualifier("dublinBusFlux")
    Flux<List<DublinBusHistorical>> dublinBusFlux;

    @GetMapping(value = "/realTimeData/{dataIndicator}")
    public void sendDataToKakfaTopic(@PathVariable(value = "dataIndicator") final String dataIndicator) {
        if (dataIndicator.equals("bike"))
            service.processRealTimeDataForDublinBikes();
    }

    @GetMapping(value = "/getRealTimeDataForBike", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<DublinBike[]> streamRealTimeBikeData() {
        return flux;
    }

    @GetMapping(value = "/getRealTimeDataForBus", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<List<DublinBusHistorical>> streamRealTimeBusData() {
        return dublinBusFlux;
    }
}
