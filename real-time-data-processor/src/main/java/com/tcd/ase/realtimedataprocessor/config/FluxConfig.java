package com.tcd.ase.realtimedataprocessor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Configuration
public class FluxConfig {

    @Bean
    public Sinks.Many<Object[]> sink() {
        return Sinks.many().replay().latest();
    }

    @Bean
    public Flux<Object[]> flux(Sinks.Many<Object[]> sink) {
        return sink.asFlux().cache();
    }

}
