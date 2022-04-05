package com.tcd.ase.realtimedataprocessor.producers;

import com.tcd.ase.realtimedataprocessor.models.DublinBikeResponseDTO;
import com.tcd.ase.realtimedataprocessor.models.DublinBike;
import com.tcd.ase.realtimedataprocessor.models.DublinBikes;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.RestTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Service
@Log4j2
public class DublinBikesProducer {

    @Autowired
    private KafkaTemplate<String, DublinBike[]> kakfaTemplate;

    public ListenableFuture<SendResult<String, DublinBike[]>> sendMessage(String topic, DublinBike[] message) {
        log.info("[BIKE] Producing Message of " + message.length + " elements");
        return this.kakfaTemplate.send(topic, message);
    }

}
