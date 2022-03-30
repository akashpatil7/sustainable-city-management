package com.tcd.ase.realtimedataprocessor.consumers;

import com.tcd.ase.realtimedataprocessor.models.Aqi;
import com.tcd.ase.realtimedataprocessor.producers.AqiProducer;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

@Service
@Log4j2
public class AqiConsumer {

    @Autowired
    private Sinks.Many<Aqi[]> aqiSink;

    @KafkaListener(topics = "aqi", groupId = "mygroup")
    public void consume(Aqi[] message) {
        log.info("[AQI] Consumed Message of " + message.length + " elements");
        this.aqiSink.tryEmitNext(message);
    }
}
