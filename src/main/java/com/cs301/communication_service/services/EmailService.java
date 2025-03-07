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
        // System.out.println(region);
        // System.out.println(Region.of(region));
        this.sesClient = SesClient.builder()
                .region(Region.of(region))
                .build();
    }

    public void sendEmail(String recipient, String subject, String messageBody) {
        // System.out.println(recipient);
        // System.out.println(subject);
        // System.out.println(messageBody);
        // System.out.println(senderEmail);
        // System.out.println();
        // Build email request
        SendEmailRequest emailRequest = SendEmailRequest.builder()
                .destination(Destination.builder().toAddresses(recipient).build())
                .message(Message.builder()
                        .subject(Content.builder().data(subject).build())
                        .body(Body.builder().text(Content.builder().data(messageBody).build()).build())
                        .build())
                .source(senderEmail)
                .build();

        //sesClient.sendEmail(emailRequest);
        try {
            System.out.println("Attempting to send an email through Amazon SES " + "using the AWS SDK for Java...");
            sesClient.sendEmail(emailRequest);

        } catch (SesException e) {
            System.out.println("error sending email...");
            System.err.println(e.awsErrorDetails().errorMessage());
            //System.exit(1);
        }
    }
}

