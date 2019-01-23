package com.codeosseum.miles.communication.websocket.handler;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

@FunctionalInterface
public interface OnSignalHandler {
    void handle(Session session) throws IOException;
}
