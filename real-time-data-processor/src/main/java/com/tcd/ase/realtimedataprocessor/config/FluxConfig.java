package com.tcd.ase.realtimedataprocessor.config;

import com.tcd.ase.realtimedataprocessor.models.Aqi;
import com.tcd.ase.realtimedataprocessor.models.DublinBike;
import com.tcd.ase.realtimedataprocessor.models.Pedestrian;
import com.tcd.ase.realtimedataprocessor.models.PedestrianCount;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

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
    public Flux<Aqi[]> bikeFlux(Sinks.Many<Aqi[]> sink) {
        return sink.asFlux().cache();
    }

    @Bean
    public Flux<DublinBike[]> aqiFlux(Sinks.Many<DublinBike[]> sink) {
        return sink.asFlux().cache();
    }
    
    @Bean
    public Flux<PedestrianCount[]> pedestrianFlux(Sinks.Many<PedestrianCount[]> sink) {
        return sink.asFlux().cache();
    }

}
