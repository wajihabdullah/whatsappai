package com.wajih.whatsappai.Builder;

import com.wajih.whatsappai.DTO.ChatHistory;
import com.wajih.whatsappai.Model.Message;

import java.util.ArrayList;
import java.util.List;

public class PromptBuilder {
    public static List<ChatHistory> buildPrompt(List<Message> prevChatHistory){
        List<ChatHistory> chatHistoryList= new ArrayList<ChatHistory>();
        for(Message m : prevChatHistory){
            ChatHistory chatHistory = new ChatHistory();
            chatHistory.setUserMessage(m.getMessage());
            chatHistory.setAiResponse(m.getAiResponse().getResponse());
            chatHistoryList.add(chatHistory);
        }
        return chatHistoryList;
    }
}
