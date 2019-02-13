package com.codeosseum.miles.communication;

import static java.util.Objects.requireNonNull;

public class Message<T> {
    private final String action;

    private final T payload;

    public static Message<Void> emptyMessage(final String action) {
        return new Message<>(requireNonNull(action), null);
    }

    public static <T> Message<T> message(final String action, final T payload) {
        return new Message<>(requireNonNull(action), requireNonNull(payload));
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
