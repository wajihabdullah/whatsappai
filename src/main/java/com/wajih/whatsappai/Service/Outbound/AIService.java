package com.wajih.whatsappai.Service.Outbound;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wajih.whatsappai.DTO.ChatHistory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper; // Inject ObjectMapper to convert objects to JSON

    // --- Configuration properties from your application.properties ---
    @Value("${azure.openai.endpoint}")
    private String azureEndpoint;

    @Value("${azure.openai.api-key}")
    private String apiKey;

    @Value("${azure.openai.deployment-name}")
    private String deploymentName;

    @Value("${system.context}")
    private String systemContext;

    public String getSystemContext() {
        return systemContext;
    }
    public String getAzureEndpoint(){
        return azureEndpoint;
    }
    public String getApiKey(){
        return apiKey;
    }
    public String getDeploymentName(){
        return deploymentName;
    }

    /**
     * Gets a response from the Azure OpenAI model using a structured conversation history.
     *
     * @param chatHistory A list of previous user messages and AI responses.
     * @param userQuery The latest message from the user.
     * @return The AI's response as a String.
     */
    public String getAIResponse(List<ChatHistory> chatHistory, String userQuery, String chatContext) {
        // Construct the full API URL
        String url = String.format("%s/openai/deployments/%s/chat/completions?api-version=2024-02-01",
                getAzureEndpoint(), getDeploymentName());

        // 1. Build the list of messages for the request payload in the correct order
        List<Message> messages = new ArrayList<>();

        // Add the main system instruction
        messages.add(new Message("system", getSystemContext()));
        if(chatContext!=null&&chatContext!=""){
            messages.add(new Message("system", chatContext));
        }
        // Add the past conversation history
        for (ChatHistory turn : chatHistory) {
            messages.add(new Message("user", turn.getUserMessage()));
            messages.add(new Message("assistant", turn.getAiResponse()));
        }

        // Add the user's latest query
        messages.add(new Message("user", userQuery));

        AzureRequest requestPayload = new AzureRequest(messages);

        // Set the required headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", getApiKey());

        HttpEntity<AzureRequest> entity = new HttpEntity<>(requestPayload, headers);

        try {

            try {
                String jsonPayload = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestPayload);
                log.info("Sending JSON Payload to Azure OpenAI:\n{}", jsonPayload);
            } catch (JsonProcessingException e) {
                log.error("Could not serialize request payload to JSON for debugging.", e);
            }

            log.info("Sending request to Azure OpenAI with {} history turns.", chatHistory.size());
            AzureResponse response = restTemplate.postForObject(url, entity, AzureResponse.class);

            // Extract the content from the first choice in the response
            if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
                String aiContent = response.getChoices().get(0).getMessage().getContent();
                log.info("Received successful response from Azure OpenAI.");
                return aiContent;
            } else {
                log.warn("Received an empty or invalid response from Azure OpenAI.");
                return "Sorry, I couldn't process that response.";
            }
        } catch (Exception e) {
            log.error("Error calling Azure OpenAI service", e);
            return "Sorry, there was an error contacting the AI service.";
        }
    }

    // --- DTOs (Data Transfer Objects) ---

    /**
     * Represents one turn of the conversation (user message + AI response).
     * This is the object you will pass in the List<ChatHistory>.
     */

    // --- Internal DTOs for the Azure API structure ---

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class AzureRequest {
        private List<Message> messages;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Message {
        private String role;
        private String content;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class AzureResponse {
        private List<Choice> choices;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Choice {
        private Message message;
    }
}
