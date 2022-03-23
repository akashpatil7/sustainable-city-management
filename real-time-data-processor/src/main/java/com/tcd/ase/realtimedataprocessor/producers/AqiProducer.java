package com.tcd.ase.realtimedataprocessor.producers;

import com.tcd.ase.realtimedataprocessor.models.Aqi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Service
public class AqiProducer {

    private static final Logger log = LogManager.getLogger(AqiProducer.class);

    @Autowired
    private KafkaTemplate<String,Aqi[]> kakfaTemplate;

    public ListenableFuture<SendResult<String,Aqi[]>> sendMessage(String topic, Aqi[] message) {
        log.info(String.format("#### -> Producing message -> %s", message));
        return this.kakfaTemplate.send(topic, message);
    }

}
