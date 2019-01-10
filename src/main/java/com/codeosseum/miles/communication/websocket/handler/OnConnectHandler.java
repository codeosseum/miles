package com.codeosseum.miles.communication.websocket.handler;

import org.eclipse.jetty.websocket.api.Session;

public interface OnConnectHandler {
    void handle(Session session);
}
