package com.tcd.ase.realtimedataprocessor.producers;

import com.tcd.ase.realtimedataprocessor.entity.DublinBusHistorical;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.List;

@Service
@Log4j2
public class DublinBusProducer {

    @Autowired
    @Qualifier("kafkaTemplateForDublinBus")
    private KafkaTemplate<String, List<String>> kafkaTemplate;

    public ListenableFuture<SendResult<String, List<String>>> sendMessage(String topic, List<String> message) {
        log.info(String.format("#### -> Producing message on Dublin Bus Topic-> %s", message));
        return this.kafkaTemplate.send(topic, message);
    }

}
