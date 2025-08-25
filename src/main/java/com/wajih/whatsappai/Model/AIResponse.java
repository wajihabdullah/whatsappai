package com.wajih.whatsappai.Model;

import com.wajih.whatsappai.Repository.MessageRepository;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "airesponses")
public class AIResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private long id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false,columnDefinition = "TEXT")
    private String response;
    @Column(nullable = false)
    private String sentTo;

    @Column(nullable = false)
    private Instant timestamp;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="message_id")
    private Message originalMessage;
}
