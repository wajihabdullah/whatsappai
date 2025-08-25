package com.wajih.whatsappai.Service.Inbound;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wajih.whatsappai.Model.Message;
import com.wajih.whatsappai.Model.WhatsappEvent;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
@AllArgsConstructor
@Service
public class InboundMessageHandlerService {
    @Autowired
    private ObjectMapper objectMapper;
    @Async
    public void handleIncomingMessage(WhatsappEvent whatsappEvent) {
        try {
            String messageString = objectMapper.writeValueAsString(whatsappEvent.getPayload());
            Message message = objectMapper.readValue((String) messageString, Message.class);
            message.setUsername(whatsappEvent.getUsername());
            message.setTimestamp(Instant.now());
            System.out.println("Message: " + message.getMessage());
        } catch (JsonProcessingException e) {
            if(e.getCause() instanceof JsonMappingException){
                log.error("Error occurred while mapping message: "+e.getMessage());
            }else {
                log.error("Error occurred while handling message: " + e.getMessage());
            }
        }
    }
}
