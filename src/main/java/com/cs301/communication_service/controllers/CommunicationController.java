package com.cs301.communication_service.controllers;

import com.cs301.communication_service.services.impl.CommunicationServiceImpl;
import com.cs301.communication_service.constants.CommunicationStatus;
import com.cs301.communication_service.dtos.CommunicationStatusResponse;
import com.cs301.communication_service.mappers.CommunicationMapper;
import com.cs301.communication_service.dtos.*;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/communications")
public class CommunicationController {
    private static final Logger logger = LoggerFactory.getLogger(CommunicationController.class);

    private final CommunicationServiceImpl communicationService;

    public CommunicationController(CommunicationServiceImpl communicationService) {
        this.communicationService = communicationService;
        logger.info("CommunicationController initialised");
    }

    @GetMapping("/{communicationId}/status")
    public ResponseEntity<CommunicationStatusResponse> getCommunicationStatus(@PathVariable String communicationId) {
        CommunicationStatus status = communicationService.getCommunicationStatus(UUID.fromString(communicationId));
        return ResponseEntity.ok(new CommunicationStatusResponse(status.name()));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<RestCommunicationDTO>> getCommunicationsForAgent(@PathVariable String userId) {
        List<RestCommunicationDTO> comms = communicationService.getRestCommunicationsDTOs(userId);
        return ResponseEntity.status(HttpStatus.SC_OK).body(comms);
    }

    
}
