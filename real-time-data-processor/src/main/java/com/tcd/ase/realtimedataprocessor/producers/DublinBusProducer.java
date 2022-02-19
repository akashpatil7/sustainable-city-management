package com.tcd.ase.realtimedataprocessor.producers;

import com.tcd.ase.realtimedataprocessor.entity.DublinBusHistorical;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.List;

public class DublinBusProducer {

    private static final Logger LOGGER = LogManager.getLogger(DublinBikesProducer.class);

    @Autowired
    private KafkaTemplate<String, List<DublinBusHistorical>> kakfaTemplate;

    public ListenableFuture<SendResult<String, List<DublinBusHistorical>>> sendMessage(String topic, List<DublinBusHistorical> message) {
        LOGGER.info(String.format("#### -> Producing message on Dublin Bus Topic-> %s", message));
        return this.kakfaTemplate.send(topic, message);
    }

}
