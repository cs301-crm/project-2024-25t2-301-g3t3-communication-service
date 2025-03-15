package com.cs301.communication_service.controllers;

import com.cs301.communication_service.services.impl.CommunicationServiceImpl;
import com.cs301.communication_service.constants.CommunicationStatus;
import com.cs301.communication_service.dtos.CommunicationDTO;
import com.cs301.communication_service.dtos.CommunicationDTOResponse;
import com.cs301.communication_service.dtos.CommunicationStatusResponse;
import com.cs301.communication_service.mappers.CommunicationMapper;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/communications")
public class CommunicationController {
    private static final Logger logger = LoggerFactory.getLogger(CommunicationController.class);

    private final CommunicationServiceImpl communicationService;
    private final CommunicationMapper communicationMapper;

    public CommunicationController(CommunicationMapper communicationMapper, CommunicationServiceImpl communicationService) {
        this.communicationMapper = communicationMapper;
        this.communicationService = communicationService;
        logger.info("CommunicationController initialised");
    }

    @PostMapping(value = "/create",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommunicationDTOResponse> createCommunication(@RequestBody @Valid CommunicationDTO communicationDTO) {
        logger.debug("Received create communication request");
        var communicationModel = communicationMapper.toModel(communicationDTO);
        var crudinfo = communicationMapper.getCrudInfo(communicationDTO);
        logger.debug("Communication model mapped successfully");
        var savedCommunication = communicationService.createCommunication(communicationModel, crudinfo);
        logger.debug("Communication created successfully");
        var response = communicationMapper.toDto(savedCommunication);
        logger.debug("Response mapped successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/{communicationId}/status")
    public ResponseEntity<CommunicationStatusResponse> getCommunicationStatus(@PathVariable String communicationId) {
        CommunicationStatus status = communicationService.getCommunicationStatus(UUID.fromString(communicationId));
        return ResponseEntity.ok(new CommunicationStatusResponse(status.name()));
    }

    
}
