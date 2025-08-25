package com.wajih.whatsappai.Controller;

import com.wajih.whatsappai.Service.Inbound.WhatsappSessionService;
import com.wajih.whatsappai.Service.Outbound.QRCodeService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.Optional;

@RestController
@RequestMapping("/qr")
@RequiredArgsConstructor
public class QRCodeController {
    @Autowired
    public final QRCodeService qrCodeService;
    @Autowired
    public final WhatsappSessionService whatsappSessionService;
    @GetMapping()
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> getQRCode() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String qr = qrCodeService.getQRCode(username);
        //String qr = qrCodeService.getQRCode(username);
        if (qr.equals("NULL")) {
            return new ResponseEntity<>("Try again later", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(qr, HttpStatus.OK);
        }

    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/hasScanned")
    public ResponseEntity<Boolean> hasScanned() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return new ResponseEntity<>(qrCodeService.hasScanned(username), HttpStatus.OK);
    }
}
