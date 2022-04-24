package com.poc.websocket.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.websocket.CustomMessage;
import com.poc.websocket.MessageType;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

public class MobileClient {

    public static final String CLIENT_TYPE = "Client-Type";

    public static void main(String[] args) {
        try {
            WebSocketClient webSocketClient = new StandardWebSocketClient();

            WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
            headers.add(CLIENT_TYPE, "mobile");

            WebSocketSession webSocketSession = connectToServer(webSocketClient, headers);

            sendMessagePeriodicly(webSocketSession);

        } catch (Exception e) {
            System.out.println("Exception while accessing websockets" + e);
        }

    }

    private static WebSocketSession connectToServer(WebSocketClient webSocketClient,
                                                    WebSocketHttpHeaders headers) throws InterruptedException, ExecutionException {
        return webSocketClient.doHandshake(new TextWebSocketHandler() {
            @Override
            public void handleTextMessage(WebSocketSession session, TextMessage message) {
                System.out.println("received message - " + message.getPayload());
            }

            @Override
            public void afterConnectionEstablished(WebSocketSession session) {
                System.out.println("established connection - " + session);
            }

        }, headers, URI.create("ws://localhost:8080/app")).get();
    }

    private static void sendMessagePeriodicly(WebSocketSession webSocketSession) {
        newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            try {
                CustomMessage customMessage = new CustomMessage("Hello From Mobile Client !!", MessageType.EKG);
                TextMessage message = new TextMessage(new ObjectMapper().writeValueAsString(customMessage));
                webSocketSession.sendMessage(message);
                System.out.println("sent message - " + message.getPayload());
            } catch (Exception e) {
                System.out.println("Exception while sending a message" + e);
            }
        }, 1, 10, TimeUnit.SECONDS);
    }
}
