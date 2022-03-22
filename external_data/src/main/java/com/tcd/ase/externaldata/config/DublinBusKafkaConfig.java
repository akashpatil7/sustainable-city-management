package com.tcd.ase.externaldata.config;

import com.tcd.ase.externaldata.entity.DublinBusHistorical;
import com.tcd.ase.externaldata.model.DublinBike;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ListDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class DublinBusKafkaConfig {

    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String bootstrapServers;

    @Bean("dublinBus")
    public NewTopic dublinBus() {
        return TopicBuilder.name("dublin_bus").config("max.message.bytes", "2000000").build();
    }

    @Bean
    public Map<String, Object> producerConfigsForDublinBus() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, "2000000");
        return props;
    }

    @Bean("producerFactoryForDublinBus")
    public ProducerFactory<String, String> producerFactoryForDublinBus() {
        return new DefaultKafkaProducerFactory<>(producerConfigsForDublinBus());
    }

    @Bean("kafkaTemplateForDublinBus")
    public KafkaTemplate<String, String> kafkaTemplateForDublinBus() {
        return new KafkaTemplate<>(producerFactoryForDublinBus());
    }
}
