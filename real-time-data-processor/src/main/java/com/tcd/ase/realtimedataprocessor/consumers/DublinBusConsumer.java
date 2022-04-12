package com.tcd.ase.realtimedataprocessor.consumers;

import com.tcd.ase.realtimedataprocessor.entity.DublinBusHistorical;
import com.tcd.ase.realtimedataprocessor.service.DublinBusService;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

import java.util.List;

/**
* The class pulls Bus data from the Kafka cluster
*/

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
        log.info("[BUS] Consumed Message: " + message.toString());
        List<DublinBusHistorical> updatedBusData = dublinBusService.getDublinBusUpdate();
        dublinBusSink.tryEmitNext(updatedBusData);
    }
}
