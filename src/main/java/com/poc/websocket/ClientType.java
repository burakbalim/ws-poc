package com.poc.websocket;

import java.util.Arrays;

public enum ClientType {

    BROWSER("browser"), MOBILE("mobile");

    private final String client;

    ClientType(String client) {
        this.client = client;
    }

    public String getClient() {
        return client;
    }

    public static ClientType getFromValue(String clientType) {
        return Arrays.stream(ClientType.values()).
                filter(item -> item.client.equals(clientType)).findAny().orElseThrow(RuntimeException::new);
    }
}
