package com.tcd.ase.realtimedataprocessor.config;

import com.tcd.ase.realtimedataprocessor.entity.DublinBusHistorical;
import com.tcd.ase.realtimedataprocessor.models.Aqi;
import com.tcd.ase.realtimedataprocessor.models.DublinBike;
import com.tcd.ase.realtimedataprocessor.models.Pedestrian;
import com.tcd.ase.realtimedataprocessor.models.PedestrianCount;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.List;

@Configuration
public class FluxConfig {

    @Bean
    public Sinks.Many<DublinBike[]> bikeSink() {
        return Sinks.many().replay().latest();
    }

    @Bean
    public Sinks.Many<Aqi[]> aqiSink() {
        return Sinks.many().replay().latest();
    }
    
    @Bean
    public Sinks.Many<PedestrianCount[]> pedestrianSink() {
        return Sinks.many().replay().latest();
    }

    @Bean
    public Flux<Aqi[]> aqiFlux (Sinks.Many<Aqi[]> sink) {
        return sink.asFlux().cache(1);
    }

    @Bean
    public Flux<DublinBike[]> bikeFlux (Sinks.Many<DublinBike[]> sink) {
        return sink.asFlux().cache(1);
    }

    @Bean(name = "dublinBusSink")
    public Sinks.Many<List<DublinBusHistorical>> dublinBusSink() {
        return Sinks.many().replay().latest();
    }

    @Bean(name = "dublinBusFlux")
    public Flux<List<DublinBusHistorical>> dublinBusFlux(Sinks.Many<List<DublinBusHistorical>> sink) {
        return sink.asFlux().cache(1);
    }
    
    @Bean
    public Flux<PedestrianCount[]> pedestrianFlux(Sinks.Many<PedestrianCount[]> sink) {
        return sink.asFlux().cache(1);
    }

}
