package com.wajih.whatsappai.Repository;

import com.wajih.whatsappai.Model.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    public List<Message> findByUsername(String username);
    List<Message> findTop20ByUsernameAndSenderOrderByTimestampAsc(String username, String sender);
    // In MessageRepository.java
    @Query("SELECT m FROM Message m LEFT JOIN FETCH m.aiResponse WHERE m.username = :username AND m.sender = :sender ORDER BY m.timestamp DESC")
    List<Message> findRecentConversationHistory(String username, String sender, Pageable pageable);
}
