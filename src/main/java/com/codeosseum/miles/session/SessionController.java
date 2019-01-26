package com.codeosseum.miles.session;

import com.codeosseum.miles.communication.websocket.controller.JsonWebSocketController;
import com.codeosseum.miles.communication.websocket.dispatcher.WebSocketDispatcher;
import com.codeosseum.miles.communication.websocket.session.SessionRegistry;
import com.codeosseum.miles.communication.websocket.transmission.MessageTransmitter;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionController extends JsonWebSocketController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionController.class);

    private final SessionRegistry sessionRegistry;

    public SessionController(final Gson gson, final MessageTransmitter messageTransmitter, final SessionRegistry sessionRegistry) {
        super(gson, messageTransmitter);

        this.sessionRegistry = sessionRegistry;
    }

    @Override
    public void attach(final WebSocketDispatcher dispatcher) {
        dispatcher.attachOnConnectHandler(this::onConnectionOpen);

        dispatcher.attachOnCloseHandler(this::onConnectionClose);
    }

    private void onConnectionOpen(final Session session) {
        LOGGER.info("Received new WS connection from {}", session.getRemoteAddress());

        sessionRegistry.addActiveSession(session);
    }

    private void onConnectionClose(final Session session, final int statusCode, final String reason) {
        LOGGER.info("WS connection closed at {} with statusCode {} and reason {}", session.getRemoteAddress(), statusCode, reason);

        sessionRegistry.removeActiveSession(session);
    }
}
