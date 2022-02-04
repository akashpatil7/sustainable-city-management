package com.tcd.ase.externaldata.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Configuration
public class AppConfiguration {

    private static final String JOKE_API_ENDPOINT = "https://joke.deno.dev/";

//    @Bean
//    public WebClient webClient(){
//        return WebClient.builder()
//                    .baseUrl(JOKE_API_ENDPOINT)
//                    .build();
//    }

    @Bean
    public Sinks.Many<String> sink(){
        return Sinks.many().replay().latest();
    }

    @Bean
    public Flux<String> flux(Sinks.Many<String> sink){
        return sink.asFlux().cache();
    }

}