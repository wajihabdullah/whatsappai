package com.wajih.whatsappai.Service.Inbound;

import com.wajih.whatsappai.Model.ChatContext;
import com.wajih.whatsappai.Repository.ChatContextRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatContextService {
    private final ChatContextRepository chatContextRepository;

    public void save(ChatContext chatContext) {
        chatContextRepository.save(chatContext);
    }
    public Optional<ChatContext> findByUsername(String username) {
        return chatContextRepository.findByUsername(username);
    }
    public Optional<ChatContext> findById(Long chatContextID) {
        return chatContextRepository.findById(chatContextID);
    }

}
