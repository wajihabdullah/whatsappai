package com.wajih.whatsappai.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true) // Prevents errors from extra fields in JSON
@Table(name="messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String sender;
    @Column(nullable = false,columnDefinition = "TEXT")
    private String message;
    private String pushName;
    private Instant timestamp;
    @OneToOne(mappedBy = "originalMessage", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private AIResponse aiResponse;

}
