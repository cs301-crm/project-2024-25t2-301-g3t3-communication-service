package com.cs301.communication_service.mappers;

import com.cs301.communication_service.models.Communication;
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
                .messageBody(model.getMessageBody())
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
        model.setSubject(dto.getSubject());
        model.setMessageBody(dto.getMessageBody());
        model.setStatus(dto.getStatus());
        // model.setTimestamp(LocalDateTime.parse(dto.getTimestamp(), DATE_FORMATTER));

        return model;
    }
    
}
