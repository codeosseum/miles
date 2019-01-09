package com.codeosseum.miles.messaging.websocket.handler;

import java.io.IOException;

import com.codeosseum.miles.messaging.MalformedMessageException;
import org.eclipse.jetty.websocket.api.Session;

public interface OnMessageHandler {
    void handle(Session session, String payload) throws MalformedMessageException, IOException;
}
