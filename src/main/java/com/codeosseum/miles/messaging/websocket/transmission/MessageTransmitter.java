package com.codeosseum.miles.messaging.websocket.transmission;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;

public interface MessageTransmitter {
    void writeMessage(Session session, String action, Object payload) throws IOException;

    void writeException(Session session, Exception exception) throws IOException;
}

