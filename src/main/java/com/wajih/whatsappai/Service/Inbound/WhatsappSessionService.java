package com.wajih.whatsappai.Service.Inbound;

import com.wajih.whatsappai.Model.WhatsappSession;
import com.wajih.whatsappai.Repository.WhatsappSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WhatsappSessionService {
    private final WhatsappSessionRepository whatsappSessionRepository;

    public Optional<WhatsappSession> findBySessionId(long sessionId) {
        return whatsappSessionRepository.findById(sessionId);
    }
    public void save(WhatsappSession whatsappSession) {
        whatsappSessionRepository.save(whatsappSession);
    }
    public void delete(WhatsappSession whatsappSession) {
        whatsappSessionRepository.delete(whatsappSession);
    }
    public void deleteByUsername(String username) {
        whatsappSessionRepository.deleteByUsername(username);
    }
    public Optional<WhatsappSession> findByUsername(String username) {
        return whatsappSessionRepository.findByUsername(username);
    }

}
