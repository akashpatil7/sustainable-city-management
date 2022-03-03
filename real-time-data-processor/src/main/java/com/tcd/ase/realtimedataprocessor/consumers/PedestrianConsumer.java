package com.tcd.ase.realtimedataprocessor.consumers;

import com.tcd.ase.realtimedataprocessor.models.Pedestrian;
import com.tcd.ase.realtimedataprocessor.models.PedestrianCount;
import com.tcd.ase.realtimedataprocessor.producers.PedestrianProducer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

@Service
public class PedestrianConsumer {

    @Autowired
    private Sinks.Many<PedestrianCount[]> pedestrianSink;

    private static final Logger log = LogManager.getLogger(PedestrianProducer.class);
    //private final Logger logger = LoggerFactory.getLogger(getClass());

    @KafkaListener(topics = "pedestrian", groupId = "mygroup")
    public void consume(PedestrianCount[] message) {
        log.info(String.format("#### -> Consumed message -> %s", message));
        this.pedestrianSink.tryEmitNext(message);
    }
}
