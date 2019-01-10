package com.codeosseum.miles.messaging.websocket.handler;

import java.io.IOException;

import com.codeosseum.miles.messaging.MalformedMessageException;
import org.eclipse.jetty.websocket.api.Session;

@FunctionalInterface
public interface OnMessageHandler<T> {
    void handle(Session session, T payload) throws IOException;
}
