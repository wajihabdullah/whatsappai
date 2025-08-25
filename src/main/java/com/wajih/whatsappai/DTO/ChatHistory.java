package com.wajih.whatsappai.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatHistory {
    private String userMessage;
    private String aiResponse;
}