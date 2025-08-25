package com.wajih.whatsappai.Service.Inbound;

import com.wajih.whatsappai.Model.Message;
import com.wajih.whatsappai.Repository.MessageRepository;
import com.wajih.whatsappai.Repository.WhatsappSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    public List<Message> findByUsername(String username) {
        return messageRepository.findByUsername(username);
    }
    public List<Message> getRecentMessagesForContext(String username, String sender) {
        // Simply call the repository method we created
        return messageRepository.findTop20ByUsernameAndSenderOrderByTimestampAsc(username, sender);
    }
    public void save(Message message) {
        messageRepository.save(message);
    }


}
