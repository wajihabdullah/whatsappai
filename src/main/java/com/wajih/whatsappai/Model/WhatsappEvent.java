package com.wajih.whatsappai.Model;
import java.time.Instant;

import com.fasterxml.jackson.core.JsonParser;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WhatsappEvent {
    private String username;
    private String event;
    private Object payload; // raw, we’ll map later
    private Instant ts;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getType() { return event; }
    public void setType(String event) { this.event = event; }
    public void setPayload(Object payload) { this.payload = payload; }
}


