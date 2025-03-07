package com.cs301.communication_service.repositories;

import com.cs301.communication_service.models.Communication;
import com.cs301.communication_service.constants.CommunicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface CommunicationRepository extends JpaRepository<Communication, UUID> {

    List<Communication> findByAgentId(String agentId);
    
    List<Communication> findByClientId(String clientId);

    List<Communication> findByStatus(CommunicationStatus status);

}
