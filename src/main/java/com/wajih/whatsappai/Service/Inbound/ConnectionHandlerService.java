package com.wajih.whatsappai.Service.Inbound;

import com.wajih.whatsappai.Model.WhatsappEvent;
import com.wajih.whatsappai.Model.WhatsappSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.rmi.server.LogStream.log;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConnectionHandlerService {
    private final WhatsappSessionService sessionService;
    @Async
    public void handleIncomingConnection(WhatsappEvent event) {
        String username = event.getUsername();
        Optional<WhatsappSession> was=sessionService.findByUsername(username);
        Boolean exists=was.isPresent();
        String payload=event.getPayload().toString();
        switch (payload){
            case "connected":
                if(exists){
                    was.get().setActive(true);
                    sessionService.save(was.get());
                }else{
                    WhatsappSession newSession=new WhatsappSession();
                    newSession.setUsername(username);
                    newSession.setActive(true);
                    sessionService.save(newSession);
                }
                break;
            case "disconnected":
                if(exists){
                    was.get().setActive(false);
                    sessionService.save(was.get());
                }
                break;
            case "logged out":
                if(exists){
                    was.get().setActive(false);
                    sessionService.save(was.get());
                    System.out.println(was.get().getUsername()+" logged out");
                }
                break;
            case "already_logged_in":
                if(!exists){
                    WhatsappSession newSession=new WhatsappSession();
                    newSession.setUsername(username);
                    newSession.setActive(true);
                    sessionService.save(newSession);
                    System.out.println("NEW SESSION");
                }
                break;

                default:
                    log.error("Invalid event received: " + payload);
        }
    }
}
