package com.wajih.whatsappai.Service.Outbound;

import com.wajih.whatsappai.Model.WhatsappSession;
import com.wajih.whatsappai.Service.Inbound.WhatsappSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
@Slf4j
@Service
public class QRCodeService {
    @Autowired
    WhatsappSessionService whatsappSessionService;
    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${bailey-backend}")
    String outboundUrl;
    public String getOutboundUrl() {
        return outboundUrl;
    }
    public String getQRCode(String username){
        String url = getOutboundUrl()+"/qr"; // BaileyServer endpoint
        String requestJson = "{ \"username\": \"" + username + "\" }";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        if(response.getStatusCode() == HttpStatus.OK){
            return response.getBody();
        }else{
            log.error("Could not create QR code for user " + username);
            return "NULL";
        }

    }
    public Boolean hasScanned(String username){
        Optional<WhatsappSession> was=whatsappSessionService.findByUsername(username);
        if(was.isPresent()){
            return was.get().isActive();
        }else{
            return false;
        }
    }
}
