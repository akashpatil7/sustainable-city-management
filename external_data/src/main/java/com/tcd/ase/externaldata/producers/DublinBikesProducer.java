package com.tcd.ase.externaldata.producers;

import com.tcd.ase.externaldata.model.DublinBikeResponseDTO;
import com.tcd.ase.externaldata.model.DublinBike;
import com.tcd.ase.externaldata.model.DublinBikes;
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
public class DublinBikesProducer {

    private static final Logger log = LogManager.getLogger(DublinBikesProducer.class);

    @Autowired
    private KafkaTemplate<String,DublinBike[]> kakfaTemplate;

    public ListenableFuture<SendResult<String,DublinBike[]>> sendMessage(String topic, DublinBike[] message) {
        log.info(String.format("#### -> Producing message -> %s", message));
        return this.kakfaTemplate.send(topic, message);
    }

}
