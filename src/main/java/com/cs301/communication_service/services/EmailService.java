package com.cs301.communication_service.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

@Service
public class EmailService {

    private final SesClient sesClient;

    @Value("${aws.ses.senderEmail}")
    private String senderEmail;

    public EmailService(@Value("${aws.region}") String region) {
        this.sesClient = SesClient.builder()
                .region(Region.of(region))
                .build();
    }

    public void sendEmail(String recipient, String subject, String body, String crudType) {
        SendEmailRequest emailRequest = SendEmailRequest.builder()
                .destination(Destination.builder().toAddresses(recipient).build())
                .message(Message.builder()
                        .subject(Content.builder().data(subject).build())
                        .body(Body.builder().text(Content.builder().data(body).build()).build())
                        .build())
                .source(senderEmail)
                .build();

        sesClient.sendEmail(emailRequest);
    }
}

