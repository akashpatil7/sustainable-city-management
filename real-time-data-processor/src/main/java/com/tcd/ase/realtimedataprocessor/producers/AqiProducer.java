package com.tcd.ase.realtimedataprocessor.producers;

import com.tcd.ase.realtimedataprocessor.models.Aqi;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Service
@Log4j2
public class AqiProducer {

    @Autowired
    private KafkaTemplate<String,Aqi[]> kakfaTemplate;

    public ListenableFuture<SendResult<String,Aqi[]>> sendMessage(String topic, Aqi[] message) {
        log.info("[AQI] Producing Message of " + message.length + " elements");
        return this.kakfaTemplate.send(topic, message);
    }

}
