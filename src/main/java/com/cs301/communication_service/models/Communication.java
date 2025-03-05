package com.cs301.communication_service.models;

import com.cs301.communication_service.constants.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "communications")
public class Communication {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "VARCHAR(36)")
    private String id;

    @NotBlank
    private String agentId;  // The agent performing the action

    @NotBlank
    private String clientId;  // The client receiving the communication

    @NotBlank
    @Email
    private String clientEmail;  // The client's email address

    @NotNull
    @Enumerated(EnumType.STRING)
    private CRUDType crudType;

    @NotBlank
    private String subject;  // Subject of the email

    @NotBlank
    @Lob
    private String messageBody;  // Content of the email

    @NotNull
    @Enumerated(EnumType.STRING)
    private CommunicationStatus status;  // SENT, FAILED, PENDING

    @NotNull
    private LocalDateTime timestamp;  // When the email was sent
}
