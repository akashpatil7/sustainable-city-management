package com.tcd.ase.realtimedataprocessor.consumers;

import com.tcd.ase.realtimedataprocessor.models.Pedestrian;
import com.tcd.ase.realtimedataprocessor.models.PedestrianCount;
import com.tcd.ase.realtimedataprocessor.producers.PedestrianProducer;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

@Service
@Log4j2
public class PedestrianConsumer {

    @Autowired
    private Sinks.Many<PedestrianCount[]> pedestrianSink;
    
    @KafkaListener(topics = "pedestrian", groupId = "mygroup")
    public void consume(PedestrianCount[] message) {
        log.info("Pedestrian: Consumed Message of " + message.length + " elements");
        this.pedestrianSink.tryEmitNext(message);
    }
}
