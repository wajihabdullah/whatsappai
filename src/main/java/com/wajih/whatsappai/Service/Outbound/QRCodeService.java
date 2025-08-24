package com.wajih.whatsappai.Service.Outbound;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class QRCodeService {

    private final WebClient webClient;

    @Value("${bailey-backend}")
    private String outboundUrl;

    // Inject WebClient.Builder to create a configured WebClient instance
    public QRCodeService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(outboundUrl).build();
    }

    /**
     * Asynchronously gets the QR code from the Baileys backend.
     *
     * @param username The username for which to request the QR code.
     * @return A Mono<String> that will emit the response body or "NULL" on error.
     */
    public Mono<String> getQRCode(String username) {
        String url = "/qr"; // The endpoint path

        Map<String, String> requestBody = Map.of("username", username);

        return this.webClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve() // Sends the request and retrieves the response
                .bodyToMono(String.class) // Extracts the body to a Mono<String>
                .onErrorResume(error -> {
                    // Log the error for debugging purposes
                    System.err.println("Failed to get QR code for " + username + ": " + error.getMessage());
                    // Return a default value in case of any error (e.g., 4xx or 5xx)
                    return Mono.just("NULL");
                });
    }
}

