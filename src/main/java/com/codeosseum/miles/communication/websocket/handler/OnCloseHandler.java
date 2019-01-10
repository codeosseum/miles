package com.codeosseum.miles.communication.websocket.handler;

import org.eclipse.jetty.websocket.api.Session;

public interface OnCloseHandler {
    void handle(Session session, int statusCode, String reason);
}
