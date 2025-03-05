package com.cs301.communication_service.repositories;

import com.cs301.communication_service.models.Communication;
import com.cs301.communication_service.constants.CommunicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommunicationRepository extends JpaRepository<Communication, String> {

    List<Communication> findByAgentId(String agentId);
    
    List<Communication> findByClientId(String clientId);

    List<Communication> findByStatus(CommunicationStatus status);

}
