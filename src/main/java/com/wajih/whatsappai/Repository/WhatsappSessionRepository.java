package com.wajih.whatsappai.Repository;


import com.wajih.whatsappai.Model.WhatsappSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WhatsappSessionRepository extends JpaRepository<WhatsappSession, Long> {
    Optional<WhatsappSession> findByUsername(String username);
    Optional<WhatsappSession> deleteByUsername(String username);
}
