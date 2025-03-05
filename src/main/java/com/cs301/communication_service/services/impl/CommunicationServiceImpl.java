package com.cs301.communication_service.services.impl;

import com.cs301.communication_service.services.CommunicationService;
import com.cs301.communication_service.constants.CommunicationStatus;
import com.cs301.communication_service.models.Communication;
import com.cs301.communication_service.repositories.CommunicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public CommunicationStatus getCommunicationStatus(String communicationId) {
        return communicationRepository.findById(communicationId)
            .map(Communication::getStatus)
            .orElseThrow(() -> new RuntimeException("Communication not found"));
    }
}
