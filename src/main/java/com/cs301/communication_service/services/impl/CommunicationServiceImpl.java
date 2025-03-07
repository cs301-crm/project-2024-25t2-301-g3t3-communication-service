package com.cs301.communication_service.services.impl;

import com.cs301.communication_service.services.CommunicationService;
import com.cs301.communication_service.constants.CommunicationStatus;
import com.cs301.communication_service.models.Communication;
import com.cs301.communication_service.repositories.CommunicationRepository;
import com.cs301.communication_service.exceptions.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class CommunicationServiceImpl implements CommunicationService {
    private final CommunicationRepository communicationRepository;

    public CommunicationServiceImpl(CommunicationRepository communicationRepository) {
        this.communicationRepository = communicationRepository;
    }

    @Override
    @Transactional
    public Communication createCommunication(Communication communication) {
        return communicationRepository.save(communication);
    }
    
    @Override
    @Transactional(readOnly = true)
    public CommunicationStatus getCommunicationStatus(UUID communicationId) {
        System.out.println(communicationId.toString());
        return communicationRepository.findById(communicationId)
            .map(Communication::getStatus)
            .orElseThrow(() -> new CommunicationNotFoundException(communicationId));
    }
}
