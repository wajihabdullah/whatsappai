package com.wajih.whatsappai.Controller;


import com.wajih.whatsappai.Model.WhatsappEvent;
import com.wajih.whatsappai.Service.Inbound.ConnectionHandlerService;
import com.wajih.whatsappai.Service.Inbound.InboundMessageHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/whatsapp")
public class WhatsappCallbackController {
    @Autowired
    private ConnectionHandlerService connectionHandlerService;
    @Autowired
    private InboundMessageHandlerService messageHandlerService;
    @PostMapping("/events")
    public ResponseEntity<Void> receiveEvents(@RequestBody WhatsappEvent event) {
        try {
            switch (event.getType()) {
                case "connection":
                    connectionHandlerService.handleIncomingConnection(event);
                    break;

                case "message":
                    messageHandlerService.handleIncomingMessage(event);
                    break;
                default:
                    System.out.println("Unhandled type, raw payload: " + event.getPayload());
            }
        } catch (Exception e) {
            System.err.println("Failed to parse payload: " + e.getMessage());
        }

//        System.out.println("=======================================");
        return ResponseEntity.ok().build();
    }

}

