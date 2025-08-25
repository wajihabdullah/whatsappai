package com.wajih.whatsappai.Service.Outbound;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboundMessageService {

    @Value("${bailey-backend}")
    private String outboundUrl;

    @Value("${node-secret}")
    private String nodeSecret; // Add the secret key from application.properties

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper; // Inject the ObjectMapper bean
    public String getOutboundUrl() {
        return outboundUrl;
    }
    public String getNodeSecret() {
        return nodeSecret;
    }
    public String sendMessageToNumber(String username, String to, String message) {
        String url = getOutboundUrl() + "/send";
        log.info("Sending outbound message to number: {}", to);

        try {
            // Create the payload for the request
            Map<String, String> payload = new HashMap<>();
            payload.put("username", username);
            payload.put("to", to);
            payload.put("message", message);

            String requestJson = objectMapper.writeValueAsString(payload);

            // Set the required headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Secret-Key", getNodeSecret()); // Use the secret key for security

            HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

            // Send the request
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return "sent successfully";
            } else {
                log.warn("Baileys service returned non-OK status: {}", response.getStatusCode());
                return "NULL";
            }

        } catch (JsonProcessingException e) {
            log.error("Error occurred while serializing outbound message: {}", e.getMessage());
            return "ERROR: JSON processing error";
        } catch (Exception e) {
            log.error("Error sending message to Baileys service", e);
            return "ERROR: " + e.getMessage();
        }
    }
}
