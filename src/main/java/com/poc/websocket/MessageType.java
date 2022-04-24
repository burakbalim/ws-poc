package com.poc.websocket;

import java.util.Arrays;

public enum MessageType {

    HEALTY("HEALTY"), EKG("EKG");

    private final String type;

    MessageType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static MessageType getFromValue(String type) {
        return Arrays.stream(MessageType.values()).
                filter(item -> item.type.equals(type)).findAny().orElseThrow(RuntimeException::new);
    }
}
