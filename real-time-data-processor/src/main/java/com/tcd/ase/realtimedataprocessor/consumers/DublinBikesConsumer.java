package com.tcd.ase.realtimedataprocessor.consumers;

import com.tcd.ase.realtimedataprocessor.models.DublinBike;
import com.tcd.ase.realtimedataprocessor.producers.DublinBikesProducer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class DublinBikesConsumer {
    private static final Logger log = LogManager.getLogger(DublinBikesProducer.class);
    //private final Logger logger = LoggerFactory.getLogger(getClass());

    @KafkaListener(topics = "dublin_bike", groupId = "mygroup")
    public void consume(DublinBike[] message) {
        log.info(String.format("#### -> Consumed message -> %s", message));
    }
}
