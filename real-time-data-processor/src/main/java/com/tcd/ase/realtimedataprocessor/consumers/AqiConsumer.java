package com.tcd.ase.realtimedataprocessor.consumers;

import com.tcd.ase.realtimedataprocessor.models.Aqi;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

@Service
public class AqiConsumer {

    @Autowired
    private Sinks.Many<Aqi[]> aqiSink;

    private static final Logger log = LogManager.getLogger(AqiConsumer.class);
    //private final Logger logger = LoggerFactory.getLogger(getClass());

    @KafkaListener(topics = "aqi", groupId = "mygroup")
    public void consume(Aqi[] message) {
        log.info(String.format("#### -> Consumed message -> %s", message));
        this.aqiSink.tryEmitNext(message);
    }
}
