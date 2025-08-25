package com.wajih.whatsappai.Repository;

import com.wajih.whatsappai.Model.AIResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface AIResponseRepository extends JpaRepository<AIResponse, Long> {
    public List<AIResponse> findByUsername(String username);
    public List<AIResponse> findTop20ByUsernameAndSentToOrderByTimestampDesc(String username, String sentTo);
}
