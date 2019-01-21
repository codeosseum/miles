package com.codeosseum.miles.communication;

import java.util.Objects;

public class Message<T> {
    private final String action;

    private final T payload;

    public static <T> Message<T> message(final String action, final T payload) {
        return new Message<>(Objects.requireNonNull(action), Objects.requireNonNull(payload));
    }

    protected Message(final String action, final T payload) {
        this.action = action;
        this.payload = payload;
    }

    public String getAction() {
        return action;
    }

    public T getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "Message{" +
                "action='" + action + '\'' +
                ", payload=" + payload +
                '}';
    }
}
