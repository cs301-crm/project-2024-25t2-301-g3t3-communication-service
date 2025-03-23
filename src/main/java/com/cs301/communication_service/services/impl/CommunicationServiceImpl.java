package com.cs301.communication_service.services.impl;

import com.cs301.communication_service.services.*;
import com.cs301.communication_service.constants.*;
import com.cs301.communication_service.models.*;
import com.cs301.communication_service.repositories.*;
import com.cs301.communication_service.exceptions.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CommunicationServiceImpl implements CommunicationService {
    
    private final CommunicationRepository communicationRepository;
    private final UserCommunicationRepository userCommunicationRepository;
    private final OtpCommunicationRepository otpCommunicationRepository;    
    private final AccountCommunicationRepository accountCommunicationRepository;
    private final EmailService emailService;

    public CommunicationServiceImpl(CommunicationRepository communicationRepository, EmailService emailService, UserCommunicationRepository userCommunicationRepository, OtpCommunicationRepository otpCommunicationRepository, AccountCommunicationRepository accountCommunicationRepository) {
        this.communicationRepository = communicationRepository;
        this.emailService = emailService;
        this.userCommunicationRepository = userCommunicationRepository;
        this.otpCommunicationRepository = otpCommunicationRepository;
        this.accountCommunicationRepository = accountCommunicationRepository;
    }

    @Override
    @Transactional
    public Communication createCommunication(Communication communication, CRUDInfo crudInfo) {
        Communication savedCommunication = communicationRepository.save(communication);

        // Send HTML email notification
        emailService.sendClientEmail(
            savedCommunication.getAgentId(),
            savedCommunication.getClientEmail(),
            savedCommunication.getClientId(),
            savedCommunication.getCrudType().toString(),
            savedCommunication.getSubject(),
            crudInfo.getAttribute(),
            crudInfo.getBeforeValue(),
            crudInfo.getAfterValue(),
            crudInfo.getTimeStamp()
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

    @Override
    @Transactional
    public UserCommunication createUserCommunication(UserCommunication communication) {
        UserCommunication savedCommunication = userCommunicationRepository.save(communication);

        // Send HTML email notification
        emailService.sendUserEmail(
            savedCommunication.getUsername(),
            savedCommunication.getUserRole(),
            savedCommunication.getUserEmail(),
            savedCommunication.getTempPassword(),
            savedCommunication.getSubject()
        );

        return savedCommunication;
    }

    @Override
    @Transactional
    public OtpCommunication createOtpCommunication(OtpCommunication communication) {
        OtpCommunication savedCommunication = otpCommunicationRepository.save(communication);

        // Send HTML email notification
        emailService.sendOtpEmail(
            savedCommunication.getEmail(),
            savedCommunication.getOtp(),
            savedCommunication.getSubject()
        );

        return savedCommunication;
    }

    @Override
    @Transactional
    public AccountCommunication createAccountCommunication(AccountCommunication communication) {
        AccountCommunication savedCommunication = accountCommunicationRepository.save(communication);

        // Send HTML email notification
        emailService.sendAccountEmail(
            savedCommunication.getAgentId(),
            savedCommunication.getClientEmail(),
            savedCommunication.getClientId(),
            savedCommunication.getCrudType().toString(),
            savedCommunication.getAccountId(),
            savedCommunication.getAccountType(),
            savedCommunication.getSubject()
        );

        return savedCommunication;
    }
}
