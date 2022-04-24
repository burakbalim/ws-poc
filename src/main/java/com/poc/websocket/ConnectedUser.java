package com.poc.websocket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Getter
@AllArgsConstructor
public class ConnectedUser {

    private WebSocketSession webSocketSession;

    private ClientType clientType;
}
