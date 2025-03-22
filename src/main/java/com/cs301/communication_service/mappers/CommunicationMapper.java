package com.cs301.communication_service.mappers;


import com.cs301.communication_service.protobuf.C2C;
import com.cs301.communication_service.protobuf.Otp;
import com.cs301.communication_service.protobuf.U2C;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.cs301.communication_service.models.*;
import com.cs301.communication_service.constants.CRUDType;
import com.cs301.communication_service.constants.CommunicationStatus;
import com.cs301.communication_service.dtos.CommunicationDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CommunicationMapper {
    private static final Logger logger = LoggerFactory.getLogger(CommunicationMapper.class);

    public OtpCommunication otpToModel(ConsumerRecord<String, Otp> record) {
        Otp otpMessage = record.value();

        OtpCommunication model = new OtpCommunication();
        model.setEmail(otpMessage.getUserEmail());
        model.setOtp(otpMessage.getOtp());
        model.setSubject("Your OTP for Verification with Scrooge Bank");
        model.setStatus(CommunicationStatus.SENT);
        
        return model;
    }

    public UserCommunication u2cToModel(ConsumerRecord<String, U2C> record) {
        U2C u2cMessage = record.value();

        UserCommunication model = new UserCommunication();
        model.setUserEmail(u2cMessage.getUserEmail());
        model.setUsername(u2cMessage.getUsername());
        model.setTempPassword(u2cMessage.getTempPassword());
        model.setUserRole(u2cMessage.getUserRole());
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
