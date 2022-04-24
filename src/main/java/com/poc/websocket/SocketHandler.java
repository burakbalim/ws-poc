package com.poc.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SocketHandler extends TextWebSocketHandler {

    private final ConnectedUserCache connectedUserCache;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        System.out.println("Handle text message" + message.toString());
        String payload = message.getPayload();
        try {
            CustomMessage customMessage = new ObjectMapper().readValue(payload, CustomMessage.class);
            if (customMessage.getMessageType() != null && customMessage.getMessageType().getType()
                    .equals(MessageType.EKG.getType())) {
                List<ConnectedUser> browserClients = connectedUserCache.getFromClient(ClientType.BROWSER);
                for (ConnectedUser item : browserClients) {
                    try {
                        item.getWebSocketSession().sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(customMessage)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //the messages will be broadcasted to all users.
        //sessions.add(session);
        System.out.println("Connected" + session);

        HttpHeaders handshakeHeaders = session.getHandshakeHeaders();
        List<String> clientTypes = handshakeHeaders.get("Client-Type");
        if (clientTypes != null && !clientTypes.isEmpty()) {
            ClientType clientType = ClientType.getFromValue(clientTypes.get(0));
            connectedUserCache.add(session.getId(), new ConnectedUser(session, clientType));
        }
    }


}
