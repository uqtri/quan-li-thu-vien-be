package org.example.qlthuvien.services;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ActiveUserTracker {

    private final Set<String> sessionIds = Collections.newSetFromMap(new ConcurrentHashMap<>());

    @EventListener
    private void onSessionConnected(SessionConnectedEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        sessionIds.add(sha.getSessionId());
    }

    @EventListener
    private void onSessionDisconnected(SessionDisconnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        sessionIds.remove(sha.getSessionId());
    }

    public int getActiveSessionCount() {
        return sessionIds.size();
    }
}
