package com.tcd.ase.realtimedataprocessor.config;

import com.tcd.ase.realtimedataprocessor.entity.DublinBusHistorical;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
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
    public ProducerFactory<String, DublinBusHistorical> producerFactoryForDublinBus() {
        return new DefaultKafkaProducerFactory<>(producerConfigsForDublinBus());
    }

    @Bean("kafkaTemplateForDublinBus")
    public KafkaTemplate<String, DublinBusHistorical> kafkaTemplateForDublinBus() {
        return new KafkaTemplate<>(producerFactoryForDublinBus());
    }

    @Bean
    public Map<String, Object> consumerConfigsDublinBus() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, "2000000");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "mygroup");
        props.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, "2000000");
        return props;
    }

    @Bean("consumerFactoryForDublinBus")
    public ConsumerFactory<String, DublinBusHistorical> consumerFactoryForDublinBus() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigsDublinBus(), new StringDeserializer(),
                new JsonDeserializer<>(DublinBusHistorical.class));
    }

    @Bean("kafkaListenerContainerFactoryForDublinBus")
    public ConcurrentKafkaListenerContainerFactory<String, DublinBusHistorical> kafkaListenerContainerFactoryForDublinBus() {
        ConcurrentKafkaListenerContainerFactory<String, DublinBusHistorical> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryForDublinBus());
        return factory;
    }
}
