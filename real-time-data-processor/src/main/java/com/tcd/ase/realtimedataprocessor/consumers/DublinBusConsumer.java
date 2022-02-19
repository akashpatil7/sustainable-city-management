package com.tcd.ase.realtimedataprocessor.consumers;

import com.tcd.ase.realtimedataprocessor.entity.DublinBusHistorical;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

import java.util.List;

@Service
public class DublinBusConsumer {

    @Autowired
    private Sinks.Many<List<DublinBusHistorical>> sink;

    private static final Logger LOGGER = LogManager.getLogger(DublinBusConsumer.class);

    @KafkaListener(topics = "dublin_bus", groupId = "mygroup")
    public void consume(List<DublinBusHistorical> message) {
        LOGGER.info(String.format("#### -> Consumed message from dublin_bus -> %s", message));
        this.sink.tryEmitNext(message);
    }
}
