// package com.cs301.communication_service.services;

// import com.cs301.communication_service.constants.CommunicationStatus;
// import com.cs301.communication_service.models.Communication;
// import com.cs301.communication_service.repositories.CommunicationRepository;
// import lombok.RequiredArgsConstructor;
// import org.springframework.stereotype.Service;
// import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
// import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
// import software.amazon.awssdk.regions.Region;
// import software.amazon.awssdk.services.ses.SesClient;
// import software.amazon.awssdk.services.ses.model.*;

// import java.time.LocalDateTime;

// @Service
// @RequiredArgsConstructor
// public class EmailService {

//     private final CommunicationRepository communicationRepository;

//     private final SesClient sesClient = SesClient.builder()
//             .region(Region.US_EAST_1) // Change to your AWS SES region
//             .credentialsProvider(StaticCredentialsProvider.create(
//                     AwsBasicCredentials.create("YOUR_AWS_ACCESS_KEY", "YOUR_AWS_SECRET_KEY")
//             ))
//             .build();

//     public Communication sendEmail(String clientId, String recipientEmail, String subject, String messageBody) {
//         Communication communication = new Communication();
//         communication.setClientId(clientId);
//         communication.setRecipientEmail(recipientEmail);
//         communication.setSubject(subject);
//         communication.setMessageBody(messageBody);
//         communication.setTimestamp(LocalDateTime.now());
//         communication.setStatus(CommunicationStatus.PENDING);

//         try {
//             SendEmailRequest emailRequest = SendEmailRequest.builder()
//                     .destination(Destination.builder().toAddresses(recipientEmail).build())
//                     .message(Message.builder()
//                             .subject(Content.builder().data(subject).build())
//                             .body(Body.builder()
//                                     .text(Content.builder().data(messageBody).build())
//                                     .build())
//                             .build())
//                     .source("your-verified-email@example.com") // Must be verified in AWS SES
//                     .build();

//             sesClient.sendEmail(emailRequest);

//             // Update status to SENT
//             communication.setStatus(CommunicationStatus.SENT);
//         } catch (SesException e) {
//             // Log and update status to FAILED
//             System.err.println("Email sending failed: " + e.awsErrorDetails().errorMessage());
//             communication.setStatus(CommunicationStatus.FAILED);
//         }

//         return communicationRepository.save(communication);
//     }
// }
