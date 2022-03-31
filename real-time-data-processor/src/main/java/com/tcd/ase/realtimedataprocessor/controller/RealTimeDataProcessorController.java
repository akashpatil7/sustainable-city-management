package com.tcd.ase.realtimedataprocessor.controller;

import com.tcd.ase.realtimedataprocessor.entity.DublinBusHistorical;
import com.tcd.ase.realtimedataprocessor.models.Aqi;
import com.tcd.ase.realtimedataprocessor.models.Pedestrian;
import com.tcd.ase.realtimedataprocessor.models.PedestrianCount;
import com.tcd.ase.realtimedataprocessor.models.DublinBike;
import com.tcd.ase.realtimedataprocessor.service.AqiService;
import com.tcd.ase.realtimedataprocessor.service.PedestrianService;
import com.tcd.ase.realtimedataprocessor.service.DublinBikeService;

import com.tcd.ase.realtimedataprocessor.service.DublinBusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

//@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class RealTimeDataProcessorController {

    @Autowired
    DublinBikeService bikeService;

    @Autowired
    DublinBusService dublinBusService;

    @Autowired
    Flux<DublinBike[]> bikeFlux;

    @Autowired
    Flux<Aqi[]> aqiFlux;

    @Autowired
    AqiService aqiService;
    
    @Autowired
    PedestrianService pedestrianService;
    
    @Autowired
    Flux<PedestrianCount[]> pedestrianFlux;

    @Autowired
    @Qualifier("dublinBusFlux")
    Flux<List<DublinBusHistorical>> dublinBusFlux;

    @GetMapping(value = "/realTimeData/{dataIndicator}")
    public void sendDataToKakfaTopic(@PathVariable(value = "dataIndicator") final String dataIndicator) {
        if(dataIndicator.equals("bike"))
            bikeService.processRealTimeDataForDublinBikes();
        if(dataIndicator.equals("aqi"))
            aqiService.processRealTimeDataForAqi();
        if(dataIndicator.equals("pedestrian"))
            pedestrianService.processRealTimeDataForPedestrian();
    }

    @GetMapping(value = "/getRealTimeDataForBike", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<DublinBike[]> streamRealTimeBikeData() {
        return bikeFlux;
    }

    @GetMapping(value = "/getRealTimeDataForBus", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<List<DublinBusHistorical>> streamRealTimeBusData() {
        return dublinBusFlux;
    }

    @GetMapping(value = "/getRealTimeDataForAqi", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Aqi[]> streamRealTimeDataForAqi() {
        return aqiFlux;
    }
    
    @GetMapping(value = "/getRealTimeDataForPedestrian", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<PedestrianCount[]> streamRealTimeDataForPedestrian() {
        return pedestrianFlux;
    }

}
