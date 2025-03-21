package com.cs301.communication_service.configs;

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
import com.cs301.communication_service.protobuf.Otp;

// import software.amazon.glue.schema.registry.serializers.GlueSchemaRegistryKafkaDeserializer;
import io.confluent.kafka.serializers.protobuf.KafkaProtobufDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaOtpConsumerConfig {
    @Value("${kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value("${kafka.schema.registry}")
    private String schemaRegistryUrl;
    
    @Bean
    public ConsumerFactory<String, String> otpConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);

        // Consumer Group ID (Ensure all consumers in a group process messages evenly)
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "communication-group");

        // Deserializers
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaProtobufDeserializer.class.getName());

        // Schema Registry URL configuration
        props.put("schema.registry.url", schemaRegistryUrl);
        props.put("specific.protobuf.value.type", Otp.class.getName());
        
        // Set missing topics to non-fatal
        props.put("spring.kafka.listener.missing-topics-fatal", false);

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> 
      kafkaListenerContainerFactoryOtp() {
   
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(otpConsumerFactory());
        return factory;
    }
    
}
