package com.wajih.whatsappai.Controller;



import com.wajih.whatsappai.Model.ChatContext;
import com.wajih.whatsappai.Model.WhatsappSession;
import com.wajih.whatsappai.Service.Inbound.ChatContextService;
import com.wajih.whatsappai.Service.Inbound.WhatsappSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/chatcontext")
public class ChatContextController {
    @Autowired
    private ChatContextService chatContextService;
    @Autowired
    private WhatsappSessionService whatsappSessionService;


    @PostMapping("/setSessionChatContext")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> setChatContext(@RequestBody String chatContextDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<WhatsappSession> was = whatsappSessionService.findByUsername(authentication.getName());
        if(!was.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Optional<ChatContext> cc= chatContextService.findByUsername(authentication.getName());
        if(cc.isPresent()) {
            cc.get().setChatContext(chatContextDTO);
            chatContextService.save(cc.get());
            was.get().setChatContext(cc.get());
            whatsappSessionService.save(was.get());
        }else{
            ChatContext chatContext=new ChatContext();
            chatContext.setUsername(authentication.getName());
            chatContext.setChatContext(chatContextDTO);
            chatContextService.save(chatContext);
            was.get().setChatContext(chatContext);
            whatsappSessionService.save(was.get());
        }

        return ResponseEntity.ok().build();
    }
}

