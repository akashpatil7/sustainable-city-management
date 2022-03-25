package com.tcd.ase.realtimedataprocessor.consumers;

import com.tcd.ase.realtimedataprocessor.entity.DublinBusHistorical;
import com.tcd.ase.realtimedataprocessor.service.DublinBusService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

import java.util.Optional;
import java.util.List;

@Service
@Log4j2
public class DublinBusConsumer {

    @Autowired
    DublinBusService dublinBusService;

    @Autowired
    @Qualifier("dublinBusSink")
    private Sinks.Many<List<DublinBusHistorical>> dublinBusSink;

    @KafkaListener(topics = "dublin_bus", groupId = "mygroup")
    public void consume(List<String> message) {
        log.info(String.format("#### -> Consumed message from dublin_bus -> %s", message));
        List<DublinBusHistorical> updatedBus = dublinBusService.getDublinBusUpdate();
        dublinBusSink.tryEmitNext(updatedBus);
    }
}
