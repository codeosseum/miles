package com.codeosseum.miles.communication.websocket.handler;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;

@FunctionalInterface
public interface OnMessageHandler<T> {
    void handle(Session session, T payload) throws IOException;
}
