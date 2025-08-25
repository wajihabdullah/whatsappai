package com.wajih.whatsappai.Model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Data
@Builder
@Getter
@Setter
@Table(name="whatsappsessions")
@AllArgsConstructor
@NoArgsConstructor
public class WhatsappSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;
    @Column(nullable = false)
    private String username;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="chat_context_id",referencedColumnName = "id")
    private ChatContext chatContext;
    boolean isActive;
}
