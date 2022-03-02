package com.tcd.ase.realtimedataprocessor.config;

import com.tcd.ase.realtimedataprocessor.models.Pedestrian;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class PedestrianKafkaConfig {
    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public Map<String, Object> producerConfigsPedestrian() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, Pedestrian[]> producerFactoryPedestrian() {
        return new DefaultKafkaProducerFactory<>(producerConfigsPedestrian());
    }

    @Bean
    public KafkaTemplate<String, Pedestrian[]> kafkaTemplatePedestrian() {
        return new KafkaTemplate<>(producerFactoryPedestrian());
    }

    @Bean
    public Map<String, Object> consumerConfigsPedestrian() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "mygroup");
        return props;
    }

    @Bean
    public ConsumerFactory<String, Pedestrian[]> consumerFactoryPedestrian() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigsPedestrian(), new StringDeserializer(),
                new JsonDeserializer<>(Pedestrian[].class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Pedestrian[]> kafkaListenerContainerFactoryPedestrian() {
        ConcurrentKafkaListenerContainerFactory<String, Pedestrian[]> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryPedestrian());
        return factory;
    }

}