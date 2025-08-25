package com.wajih.whatsappai.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "airesponses")
public class AIResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private long id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String response;
    @Column(nullable = false)
    private String sentTo;
    @Column(nullable = false)
    private long initialMessageID;
}
