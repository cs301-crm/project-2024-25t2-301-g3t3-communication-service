package com.cs301.communication_service.consumer;

import com.cs301.communication_service.protobuf.Otp;
import com.cs301.communication_service.services.impl.CommunicationServiceImpl;
import com.cs301.communication_service.mappers.CommunicationMapper;
import com.cs301.communication_service.models.UserCommunication;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserConsumer {

    private final CommunicationServiceImpl communicationService;
    private final CommunicationMapper communicationMapper;

    public UserConsumer(CommunicationServiceImpl communicationService, CommunicationMapper communicationMapper) {
        this.communicationService = communicationService;
        this.communicationMapper = communicationMapper;
    }

    @KafkaListener(
        topics = "otp", 
        groupId = "communication-group", 
        containerFactory = "kafkaListenerContainerFactoryUser"
    )
    public void consumeOtp(ConsumerRecord<String, Otp> record) {
        System.out.println("received otp message!");
        UserCommunication communication = communicationMapper.otpToModel(record);
        System.out.println("converted otp message!");

        // Now process the DTO (store in DB, send email, etc.)
        communicationService.createUserCommunication(communication);
    }
}

