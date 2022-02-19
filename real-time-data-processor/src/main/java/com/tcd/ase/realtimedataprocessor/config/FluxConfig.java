package com.tcd.ase.realtimedataprocessor.config;

import com.tcd.ase.realtimedataprocessor.models.DublinBike;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Configuration
public class FluxConfig {

    @Bean
    public Sinks.Many<DublinBike[]> sink() {
        return Sinks.many().replay().latest();
    }

    @Bean
    public Flux<DublinBike[]> flux(Sinks.Many<DublinBike[]> sink) {
        return sink.asFlux().cache();
    }

}
