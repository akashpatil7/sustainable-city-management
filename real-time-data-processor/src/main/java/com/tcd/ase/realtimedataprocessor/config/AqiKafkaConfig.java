package com.tcd.ase.realtimedataprocessor.config;

import com.tcd.ase.realtimedataprocessor.models.Aqi;
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

/**

* The class configures the AQI data with Kafka

* And output

* @version 8.9

* @author Nitish Singh

*/

@Configuration
public class AqiKafkaConfig {
    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public Map<String, Object> producerConfigsAqi() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, Aqi[]> producerFactoryAqi() {
        return new DefaultKafkaProducerFactory<>(producerConfigsAqi());
    }

    @Bean
    public KafkaTemplate<String, Aqi[]> kafkaTemplateAqi() {
        return new KafkaTemplate<>(producerFactoryAqi());
    }

    @Bean
    public Map<String, Object> consumerConfigsAqi() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "mygroup");
        return props;
    }

    @Bean
    public ConsumerFactory<String, Aqi[]> consumerFactoryAqi() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigsAqi(), new StringDeserializer(),
                new JsonDeserializer<>(Aqi[].class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Aqi[]> kafkaListenerContainerFactoryAqi() {
        ConcurrentKafkaListenerContainerFactory<String, Aqi[]> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryAqi());
        return factory;
    }

}
