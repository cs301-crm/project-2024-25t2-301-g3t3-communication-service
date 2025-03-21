package com.cs301.communication_service.mappers;


import com.cs301.communication_service.protobuf.C2C;
import com.cs301.communication_service.protobuf.Otp;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import com.cs301.communication_service.models.CRUDInfo;
import com.cs301.communication_service.models.*;
import com.cs301.communication_service.constants.CRUDType;
import com.cs301.communication_service.constants.CommunicationStatus;
import com.cs301.communication_service.dtos.CommunicationDTO;
import com.cs301.communication_service.dtos.CommunicationDTOResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
//import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class CommunicationMapper {
    private static final Logger logger = LoggerFactory.getLogger(CommunicationMapper.class);

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Converts Communication model to CommunicationDTOResponse
     */
    public CommunicationDTOResponse toDto(Communication model) {
        if (model == null) return null;

        CommunicationDTOResponse dto = CommunicationDTOResponse.builder()
                .communicationId(model.getId())
                .agentId(model.getAgentId())
                .clientId(model.getClientId())
                .clientEmail(model.getClientEmail())
                .crudType(model.getCrudType())
                .subject(model.getSubject())
                // .messageBody(model.getMessageBody())
                .status(model.getStatus())
                .timestamp(model.getTimestamp().format(DATE_FORMATTER))
                .build();

        return dto;
    }

    /**
     * Converts CommunicationDTO to Communication model
     */
    public Communication toModel(CommunicationDTO dto) {
        if (dto == null) return null;

        Communication model = new Communication();
        model.setAgentId(dto.getAgentId());
        model.setClientId(dto.getClientId());
        model.setClientEmail(dto.getClientEmail());
        model.setCrudType(dto.getCrudType());
        model.setSubject(getSubjectFromCrudType(dto.getCrudType()));
        // model.setMessageBody(dto.getMessageBody());
        model.setStatus(CommunicationStatus.SENT);
        // model.setTimestamp(LocalDateTime.parse(dto.getTimestamp(), DATE_FORMATTER));

        return model;
    }

    public UserCommunication otpToModel(ConsumerRecord<String, Otp> record) {
        Otp otpMessage = record.value();

        UserCommunication model = new UserCommunication();
        model.setUserEmail(otpMessage.getUserEmail());
        model.setUsername(otpMessage.getUsername());
        model.setTempPassword(otpMessage.getTempPassword());
        model.setUserRole(otpMessage.getUserRole());
        model.setSubject("Welcome Access to Scrooge Bank CRM System");
        model.setStatus(CommunicationStatus.SENT);
        
        return model;
    }

    public Communication c2cToModel(ConsumerRecord<String, C2C> record) {
        C2C c2cMessage = record.value();

        Communication model = new Communication();
        model.setAgentId(c2cMessage.getAgentId());
        model.setClientId(c2cMessage.getClientId());
        model.setClientEmail(c2cMessage.getClientEmail());
        model.setCrudType(mapCrudType(c2cMessage.getCrudType()));
        model.setSubject(getSubjectFromCrudType(mapCrudType(c2cMessage.getCrudType())));
        model.setStatus(CommunicationStatus.SENT);

        return model;
    }

    private CRUDType mapCrudType(String protoCrudType) {
        // Convert string from Protobuf to your enum
        // e.g., "CREATE" -> CRUDType.CREATE
        try {
            return CRUDType.valueOf(protoCrudType.toUpperCase());
        } catch (Exception e) {
            return CRUDType.UPDATE; // or some default
        }
    }

    public CRUDInfo getc2cCrudInfo(ConsumerRecord<String, C2C> record) {
        C2C c2cMessage = record.value();

        CRUDInfo crudInfo = new CRUDInfo(
            c2cMessage.getCrudInfo().getAttribute(),
            c2cMessage.getCrudInfo().getBeforeValue(),
            c2cMessage.getCrudInfo().getAfterValue()
        );

        return crudInfo;
    }

    public CRUDInfo getCrudInfo(CommunicationDTO dto) {
        if (dto == null) return null;

        return dto.getCrudInfo();
    }

    public String getSubjectFromCrudType(CRUDType crudType) {
        switch (crudType) {
            case CREATE:
                return "Verify Your New Account";
            case UPDATE:
                return "Account Activity Alert";
            case DELETE:
                return "Your Account Has Been Closed";
            default:
                return "Account Activity Alert";
        }
            
    }
    
}
