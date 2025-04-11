package com.cs301.communication_service.configs;

import com.amazonaws.services.schemaregistry.deserializers.GlueSchemaRegistryKafkaDeserializer;
import com.amazonaws.services.schemaregistry.utils.AWSSchemaRegistryConstants;
import org.apache.kafka.clients.consumer.ConsumerConfig;
// import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.beans.factory.annotation.Value;
// import com.amazonaws.services.schemaregistry.serializers.protobuf.ProtobufSerializer;
import com.cs301.shared.protobuf.A2C;

// import software.amazon.glue.schema.registry.serializers.GlueSchemaRegistryKafkaDeserializer;
import io.confluent.kafka.serializers.protobuf.KafkaProtobufDeserializer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.glue.model.DataFormat;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaAccountConsumerConfig {
    @Value("${kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value("${kafka.schema.registry}")
    private String schemaRegistryUrl;

    @Value("${kafka.schema.protobuf.a2c}")
    private String protobufA2CSchema;
    
    @Bean
    public ConsumerFactory<String, String> accountConsumerFactory() {
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
        props.put(AWSSchemaRegistryConstants.SCHEMA_NAME, protobufA2CSchema);

        props.put("auto.register.schemas", false);
        props.put("specific.protobuf.value.type", A2C.class.getName());
        
        // Set missing topics to non-fatal
        props.put("spring.kafka.listener.missing-topics-fatal", false);

        // Set consumer to read from earliest message to maintain FIFO order
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> 
      kafkaListenerContainerFactoryAccount() {
   
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(accountConsumerFactory());
        return factory;
    }
    
}
