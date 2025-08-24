package com.wajih.whatsappai.Controller;

import com.wajih.whatsappai.Model.WhatsappSession;
import com.wajih.whatsappai.Service.Inbound.WhatsappSessionService;
import com.wajih.whatsappai.Service.Outbound.QRCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequestMapping("/qr")
@RequiredArgsConstructor
public class QRCodeController {
    @Autowired
    private final QRCodeService qrCodeService;
    @Autowired
    private final WhatsappSessionService whatsappSessionService;

    @GetMapping()
    @PreAuthorize("hasRole('USER')")
    public Mono<ResponseEntity<String>> getQRCode() {
        // 1. Get the current user's name.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // 2. Call the service. This doesn't block. It immediately returns a Mono (the buzzer).
        Mono<String> qrMono = qrCodeService.getQRCode(username);

        // 3. Use .map() to define what happens when the Mono completes (when the buzzer goes off).
        return qrMono.map(qr -> {
            // This code runs LATER, when the QR code string is ready.
            if ("NULL".equals(qr)) {
                // If the result is "NULL", create a "Not Found" response.
                return new ResponseEntity<>("Try again later", HttpStatus.NOT_FOUND);
            } else {
                // Otherwise, create an "OK" response with the QR code.
                return new ResponseEntity<>(qr, HttpStatus.OK);
            }
        });
    }
    public ResponseEntity<Boolean> hasScanned(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<WhatsappSession> sessionToCheck=whatsappSessionService.findByUsername(username);
        if(sessionToCheck.isPresent()){
            return new ResponseEntity<>(sessionToCheck.get().isActive(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
    }
}