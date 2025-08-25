package com.wajih.whatsappai.Service.Inbound;

import com.wajih.whatsappai.Model.WhatsappEvent;
import com.wajih.whatsappai.Model.WhatsappSession;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

                default:
                    System.out.println("Invalid event received");
        }
    }
}
