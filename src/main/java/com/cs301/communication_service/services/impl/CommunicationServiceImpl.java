package com.cs301.communication_service.services.impl;

import com.cs301.communication_service.services.*;
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
    private final EmailService emailService;

    public CommunicationServiceImpl(CommunicationRepository communicationRepository, EmailService emailService) {
        this.communicationRepository = communicationRepository;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public Communication createCommunication(Communication communication) {
        Communication savedCommunication = communicationRepository.save(communication);

        // Format email message
        String emailBody = String.format(
            "Dear Customer (%s),\n\n%s\n\nThis is an automated email. Please do not reply.\n\nThank you for banking with us.\n\nYours faithfully,\nScrooge Bank",
            savedCommunication.getClientId(),
            savedCommunication.getMessageBody()
        );

        // System.out.println(emailBody);

        // Send email notification
        emailService.sendEmail(
            savedCommunication.getClientEmail(),
            savedCommunication.getSubject(),
            emailBody
        );

        return savedCommunication;
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
