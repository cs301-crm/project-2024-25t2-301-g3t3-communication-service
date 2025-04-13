package com.cs301.communication_service.configs;

import com.amazonaws.services.schemaregistry.deserializers.GlueSchemaRegistryKafkaDeserializer;
import com.amazonaws.services.schemaregistry.utils.AWSSchemaRegistryConstants;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.beans.factory.annotation.Value;
import com.cs301.shared.protobuf.C2C;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.glue.model.DataFormat;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaClientConsumerConfig {
    @Value("${kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value("${kafka.schema.registry}")
    private String schemaRegistryUrl;

    @Value("${kafka.schema.protobuf.c2c}")
    private String protobufC2CSchema;

    @Bean
    public ConsumerFactory<String, String> clientConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);

        // Consumer Group ID (Ensure all consumers in a group process messages evenly)
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "communication-group");

        // Deserializers
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GlueSchemaRegistryKafkaDeserializer.class.getName());
        props.put(AWSSchemaRegistryConstants.AWS_REGION, Region.AP_SOUTHEAST_1);
        props.put(AWSSchemaRegistryConstants.DATA_FORMAT, DataFormat.PROTOBUF.name());
        props.put(AWSSchemaRegistryConstants.REGISTRY_NAME, schemaRegistryUrl);
        props.put(AWSSchemaRegistryConstants.SCHEMA_NAME, protobufC2CSchema);

        // Set consumer to read from earliest message to maintain FIFO order
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> 
      kafkaListenerContainerFactoryClient() {
   
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(clientConsumerFactory());
        return factory;
    }
    
}
