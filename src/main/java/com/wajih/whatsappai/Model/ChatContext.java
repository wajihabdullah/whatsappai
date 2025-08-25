package com.wajih.whatsappai.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
@Entity
@Getter
@Setter
@Data
@RequiredArgsConstructor
@Table(name = "chatcontexts")
public class ChatContext {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false,columnDefinition = "TEXT")
    private String chatContext;
}
