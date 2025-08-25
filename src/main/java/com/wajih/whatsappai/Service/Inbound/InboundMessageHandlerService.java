package com.wajih.whatsappai.Service.Inbound;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wajih.whatsappai.Builder.PromptBuilder;
import com.wajih.whatsappai.DTO.ChatHistory;
import com.wajih.whatsappai.Model.AIResponse;
import com.wajih.whatsappai.Model.Message;
import com.wajih.whatsappai.Model.WhatsappEvent;
import com.wajih.whatsappai.Model.WhatsappSession;
import com.wajih.whatsappai.Service.Outbound.AIService;
import com.wajih.whatsappai.Service.Outbound.OutboundMessageService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@AllArgsConstructor
@Service
public class InboundMessageHandlerService {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MessageService messageService;
    @Autowired
    private AIService aiService;
    @Autowired
    OutboundMessageService outboundMessageService;
    @Autowired
    WhatsappSessionService whatsappSessionService;
    @Async
    public void handleIncomingMessage(WhatsappEvent whatsappEvent) {
        try {
            String messageString = objectMapper.writeValueAsString(whatsappEvent.getPayload());
            Message message = objectMapper.readValue((String) messageString, Message.class);
            message.setUsername(whatsappEvent.getUsername());
            message.setTimestamp(Instant.now());
            List<Message> messageList=messageService.getRecentMessagesForContext(whatsappEvent.getUsername(), message.getSender());
            List<ChatHistory>chatHistory=PromptBuilder.buildPrompt(messageList);
            Optional<WhatsappSession> was=whatsappSessionService.findByUsername(message.getUsername());
            String aiResp;
            if(was.isPresent()) {
                aiResp=aiService.getAIResponse(chatHistory, message.getMessage(), was.get().getChatContext().getChatContext());
            }else{
                aiResp=aiService.getAIResponse(chatHistory, message.getMessage(), "");
            }
            AIResponse aiResponse=new AIResponse();
            aiResponse.setUsername(message.getUsername());
            aiResponse.setTimestamp(Instant.now());
            aiResponse.setSentTo(message.getSender());
            aiResponse.setOriginalMessage(message);
            aiResponse.setResponse(aiResp);
            message.setAiResponse(aiResponse);
            messageService.save(message);
            outboundMessageService.sendMessageToNumber(message.getUsername(),message.getSender(),aiResp);
        } catch (JsonProcessingException e) {
            if(e.getCause() instanceof JsonMappingException){
                log.error("Error occurred while mapping message: "+e.getMessage());
            }else {
                log.error("Error occurred while handling message: " + e.getMessage());
            }
        }
    }
}
