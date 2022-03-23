package com.tcd.ase.realtimedataprocessor.producers;

import com.tcd.ase.realtimedataprocessor.models.Pedestrian;
import com.tcd.ase.realtimedataprocessor.models.PedestrianCount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Service
public class PedestrianProducer {

    private static final Logger log = LogManager.getLogger(PedestrianProducer.class);

    @Autowired
    private KafkaTemplate<String,PedestrianCount[]> kakfaTemplate;

    public ListenableFuture<SendResult<String,PedestrianCount[]>> sendMessage(String topic, PedestrianCount[] message) {
        log.info(String.format("#### -> Producing message -> %s", message));
        return this.kakfaTemplate.send(topic, message);
    }

}
