package com.tcd.ase.realtimedataprocessor.controller;

import com.tcd.ase.realtimedataprocessor.models.Aqi;
import com.tcd.ase.realtimedataprocessor.models.DublinBike;
import com.tcd.ase.realtimedataprocessor.service.AqiService;
import com.tcd.ase.realtimedataprocessor.service.DublinBikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class RealTimeDataProcessorController {

    @Autowired
    DublinBikeService bikeService;

    @Autowired
    Flux<DublinBike[]> bikeFlux;

    @Autowired
    Flux<Aqi[]> aqiFlux;

    @Autowired
    AqiService aqiService;

    @GetMapping(value = "/realTimeData/{dataIndicator}")
    public void sendDataToKakfaTopic(@PathVariable(value = "dataIndicator") final String dataIndicator) {
        if(dataIndicator.equals("bike"))
            bikeService.processRealTimeDataForDublinBikes();
        if(dataIndicator.equals("aqi"))
            aqiService.processRealTimeDataForAqi();
    }

    @GetMapping(value = "/getRealTimeDataForBike", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<DublinBike[]> streamRealTimeDataForBike() {
        return bikeFlux;
    }

    @GetMapping(value = "/getRealTimeDataForAqi", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Aqi[]> streamRealTimeDataForAqi() {
        return aqiFlux;
    }


}
