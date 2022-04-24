package com.poc.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//MOVE TO redis or hazelcast
@Component
public class ConnectedUserCache {

    private final Map<String, ConnectedUser> SESSION_MAP = new HashMap<>();

    public void add(String sessionId, ConnectedUser connectedUser) {
        SESSION_MAP.put(sessionId, connectedUser);
    }

    public List<ConnectedUser> getFromClient(ClientType clientType) {
        return SESSION_MAP.values().stream().filter(connectedUser -> connectedUser.getClientType().equals(clientType)).collect(Collectors.toList());
    }
}
