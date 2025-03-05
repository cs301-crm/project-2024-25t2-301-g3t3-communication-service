package com.cs301.communication_service.controllers;

import com.cs301.communication_service.models.Communication;
import com.cs301.communication_service.repositories.CommunicationRepository;
import com.cs301.communication_service.services.CommunicationService;
import com.cs301.communication_service.constants.CRUDType;
import com.cs301.communication_service.constants.CommunicationStatus;
import com.cs301.communication_service.dtos.CommunicationDTO;
import com.cs301.communication_service.mappers.CommunicationMapper;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/communications")
public class CommunicationController {
    private static final Logger logger = LoggerFactory.getLogger(CommunicationController.class);

    private final CommunicationService communicationService;
    private final CommunicationMapper communicationMapper;

    public CommunicationController(CommunicationMapper communicationMapper, CommunicationService communicationService) {
        this.communicationMapper = communicationMapper;
        this.communicationService = communicationService;
        logger.info("CommunicationController initialised");
    }

    @PostMapping(value = "/create",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommunicationDTO> createCommunication(@RequestBody @Valid CommunicationDTO communicationDTO) {
        logger.debug("Received create communication request");
        var communicationModel = communicationMapper.toModel(communicationDTO);
        logger.debug("Communication model mapped successfully");
        var savedCommunication = communicationService.createCommunication(communicationModel);
        logger.debug("Communication created successfully");
        var response = communicationMapper.toDto(savedCommunication);
        logger.debug("Response mapped successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
        // try {
        //     communicationService.sendEmail(
        //             communicationDTO.getClientId(),
        //             communicationDTO.getClientEmail(),
        //             communicationDTO.getSubject(),
        //             communicationDTO.getMessageBody()
        //     );
        //     return ResponseEntity.status(HttpStatus.CREATED).body("Email sent successfully.");
        // } catch (Exception e) {
        //     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email: " + e.getMessage());
        // }
    }


    @GetMapping("/{communicationId}/status")
    public ResponseEntity<String> getCommunicationStatus(@PathVariable String communicationId) {
        CommunicationStatus status = communicationService.getCommunicationStatus(communicationId);
        return ResponseEntity.ok(status.name()); // Return status as a string
    }

    
}
