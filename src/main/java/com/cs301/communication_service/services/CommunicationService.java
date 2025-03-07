package com.cs301.communication_service.services;

import com.cs301.communication_service.models.Communication;
import com.cs301.communication_service.constants.CommunicationStatus;
import java.util.*;

public interface CommunicationService {
        Communication createCommunication(Communication communication);
        CommunicationStatus getCommunicationStatus(UUID communicationId);
}