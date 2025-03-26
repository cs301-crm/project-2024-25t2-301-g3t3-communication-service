package com.cs301.communication_service.models;

import java.time.*;

import com.cs301.communication_service.constants.CommunicationStatus;

public class RestCommunication {
    private String subject;
    private LocalDateTime timeStamp;
    private CommunicationStatus commStatus;

    public RestCommunication(String subject, LocalDateTime timeStamp, CommunicationStatus commStatus) {
        this.subject = subject;
        this.commStatus = commStatus;
        this.timeStamp = timeStamp;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public String getSubject() {
        return subject;
    }

    public CommunicationStatus geCommunicationStatus() {
        return commStatus;
    }

}
