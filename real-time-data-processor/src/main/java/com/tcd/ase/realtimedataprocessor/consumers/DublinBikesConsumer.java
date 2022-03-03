package com.tcd.ase.realtimedataprocessor.consumers;

import com.tcd.ase.realtimedataprocessor.models.DublinBike;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

@Service
@Log4j2
public class DublinBikesConsumer {

    @Autowired
    private Sinks.Many<DublinBike[]> bikeSink;

    @KafkaListener(topics = "dublin_bike", groupId = "mygroup")
    public void consume(DublinBike[] message) {
        log.info(String.format("#### -> Consumed message -> %s", message));
        this.bikeSink.tryEmitNext(message);
    }
}
