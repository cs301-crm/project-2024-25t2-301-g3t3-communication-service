package com.cs301.communication_service.services;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;
import java.nio.charset.StandardCharsets;
import java.io.IOException;

import com.cs301.communication_service.configs.AppConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

@Service
public class EmailService {

    private final AppConfig appConfig;

    private final SesClient sesClient;

    @Value("${aws.ses.senderEmail}")
    private String senderEmail;

    public EmailService(@Value("${aws.region}") String region, AppConfig appConfig) {
        // System.out.println(region);
        // System.out.println(Region.of(region));
        this.sesClient = SesClient.builder()
                .region(Region.of(region))
                .build();
        // System.out.println(region);
        // System.out.println(Region.of(region));
        this.appConfig = appConfig;
    }

    private String loadEmailTemplate(String crudType) throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/crud-email-template.html");
        if (crudType.equals("CREATE")) {
            resource = new ClassPathResource("templates/create-verify-email-template.html");
        } else if (crudType.equals("DELETE")) {
            resource = new ClassPathResource("templates/delete-email-template.html");
        }
        return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
    }

    private String loadUserEmailTemplate() throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/user-email-template.html");
        return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
    }

    private String loadOtpEmailTemplate() throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/otp-user-email-template.html");
        return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
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

    public void sendClientEmail(String agentId, String recipient, String clientId, String crudType, String subject, String attribute, String beforeValue, String afterValue, String timestamp) {
        try {
            // Load HTML template
            String emailBody = loadEmailTemplate(crudType)
                    .replace("{{agent_id}}", agentId)
                    .replace("{{client_id}}", clientId)
                    .replace("{{crud_action}}", crudType)
                    .replace("{{attribute}}", capitalise(attribute))
                    .replace("{{before_value}}", beforeValue)
                    .replace("{{after_value}}", afterValue)
                    .replace("{{timestamp}}", timestamp); // Ensure correct placeholders

            //System.out.println("Sending verification email to: {}" + recipient);

            // Build AWS SES email request
            SendEmailRequest emailRequest = SendEmailRequest.builder()
                    .destination(Destination.builder().toAddresses(recipient).build())
                    .message(Message.builder()
                            .subject(Content.builder().data(subject).build())
                            .body(Body.builder().html(Content.builder().data(emailBody).build()).build()) // HTML Body
                            .build())
                    .source(senderEmail)
                    .build();
                    
            try {
                System.out.println("Attempting to send an HTML email through Amazon SES " + "using the AWS SDK for Java...");
                sesClient.sendEmail(emailRequest);
    
            } catch (SesException e) {
                System.out.println("error sending HTML email...");
                System.err.println(e.awsErrorDetails().errorMessage());
                //System.exit(1);
            }

        } catch (IOException e) {
            System.out.println("Error loading email template" + e);
        } catch (SesException e) {
            System.out.println("AWS SES Error: {}" + e.awsErrorDetails().errorMessage());
        }
    }

    public void sendUserEmail(String userName, String userRole, String userEmail, String tempPassword, String subject) {
        try {
            // Load HTML template
            String emailBody = loadUserEmailTemplate()
                    .replace("{{username}}", userName)
                    .replace("{{user_role}}", capitalise(userRole))
                    .replace("{{temp_password}}", tempPassword);

            //System.out.println("Sending verification email to: {}" + userEmail);

            // Build AWS SES email request
            SendEmailRequest emailRequest = SendEmailRequest.builder()
                    .destination(Destination.builder().toAddresses(userEmail).build())
                    .message(Message.builder()
                            .subject(Content.builder().data(subject).build())
                            .body(Body.builder().html(Content.builder().data(emailBody).build()).build()) // HTML Body
                            .build())
                    .source(senderEmail)
                    .build();
                    
            try {
                System.out.println("Attempting to send an HTML email through Amazon SES " + "using the AWS SDK for Java...");
                sesClient.sendEmail(emailRequest);
    
            } catch (SesException e) {
                System.out.println("error sending HTML email...");
                System.err.println(e.awsErrorDetails().errorMessage());
                //System.exit(1);
            }

        } catch (IOException e) {
            System.out.println("Error loading email template" + e);
        } catch (SesException e) {
            System.out.println("AWS SES Error: {}" + e.awsErrorDetails().errorMessage());
        }
    }

    public void sendOtpEmail(String email, int otp, String subject) {
        try {
            // Load HTML template
            String emailBody = loadOtpEmailTemplate()
                    .replace("{{otp}}", String.valueOf(otp));

            // Build AWS SES email request
            SendEmailRequest emailRequest = SendEmailRequest.builder()
                    .destination(Destination.builder().toAddresses(email).build())
                    .message(Message.builder()
                            .subject(Content.builder().data(subject).build())
                            .body(Body.builder().html(Content.builder().data(emailBody).build()).build()) // HTML Body
                            .build())
                    .source(senderEmail)
                    .build();
                    
            try {
                System.out.println("Attempting to send an HTML email through Amazon SES " + "using the AWS SDK for Java...");
                sesClient.sendEmail(emailRequest);
    
            } catch (SesException e) {
                System.out.println("error sending HTML email...");
                System.err.println(e.awsErrorDetails().errorMessage());
                //System.exit(1);
            }

        } catch (IOException e) {
            System.out.println("Error loading email template" + e);
        } catch (SesException e) {
            System.out.println("AWS SES Error: {}" + e.awsErrorDetails().errorMessage());
        }
    }

    public String capitalise(String str) {
        if (str == null || str.isEmpty()) {
            return str; // Return as is if empty or null
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    // public String getSubject(String crudType) {
    //     if (crudType.equals("CREATE")) return "Verify Your New Account";
    //     return "Account Activity Alert";
    // }
}

