package com.wajih.whatsappai.Repository;

import com.wajih.whatsappai.Model.ChatContext;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatContextRepository extends JpaRepository<ChatContext, Long> {
    Optional<ChatContext> findByUsername(String username);
    Optional<ChatContext> findById(Long chatContextID);
}
