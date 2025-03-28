package com.cs301.communication_service.repositories;

import com.cs301.communication_service.models.AccountCommunication;
import com.cs301.communication_service.constants.CommunicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
import java.util.List;



public interface AccountCommunicationRepository extends JpaRepository<AccountCommunication, UUID> {

    List<AccountCommunication> findByStatus(CommunicationStatus status);
    List<AccountCommunication> findByAgentId(String agentId);
    
}