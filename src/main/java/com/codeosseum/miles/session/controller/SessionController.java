package com.codeosseum.miles.session.controller;

import com.codeosseum.miles.communication.websocket.controller.JsonWebSocketController;
import com.codeosseum.miles.communication.websocket.dispatcher.WebSocketDispatcher;
import com.codeosseum.miles.communication.websocket.session.SessionRegistry;
import com.codeosseum.miles.communication.websocket.transmission.MessageTransmitter;
import com.codeosseum.miles.player.PlayerRegistry;
import com.google.gson.Gson;
import com.google.inject.Inject;
import org.eclipse.jetty.websocket.api.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionController extends JsonWebSocketController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionController.class);

    private final SessionRegistry sessionRegistry;

    private final PlayerRegistry playerRegistry;

    @Inject
    public SessionController(final Gson gson, final MessageTransmitter messageTransmitter, final SessionRegistry sessionRegistry, final PlayerRegistry playerRegistry) {
        super(gson, messageTransmitter);

        this.sessionRegistry = sessionRegistry;
        this.playerRegistry = playerRegistry;
    }

    @Override
    public void attach(final WebSocketDispatcher dispatcher) {
        dispatcher.attachOnConnectHandler(this::onConnectionOpen);

        dispatcher.attachOnMessageHandler(HelloMessage.ACTION, HelloMessage.class, this::onHello);

        dispatcher.attachOnCloseHandler(this::onConnectionClose);

        LOGGER.info("Attached SessionController");
    }

    private void onConnectionOpen(final Session session) {
        LOGGER.info("Received new WS connection from {}", session.getRemoteAddress());

        sessionRegistry.addActiveSession(session);
    }

    private void onHello(final Session session, final HelloMessage payload) {
        final boolean canAuthenticate =
                sessionNotAuthenticated(session)
                && userNotAuthenticated(payload.getUsername())
                && userIsParticipantOfTheMatch(payload.getUsername());

        if (canAuthenticate) {
            sessionRegistry.addAuthenticatedSession(session, payload.getUsername());
        }
    }

    private void onConnectionClose(final Session session, final int statusCode, final String reason) {
        LOGGER.info("WS connection closed at {} with statusCode {} and reason {}", session.getRemoteAddress(), statusCode, reason);

        sessionRegistry.removeActiveSession(session);
    }

    private boolean sessionNotAuthenticated(final Session session) {
        return !sessionRegistry.getIdForSession(session).isPresent();
    }

    private boolean userNotAuthenticated(final String username) {
        return !sessionRegistry.getSessionForId(username).isPresent();
    }

    private boolean userIsParticipantOfTheMatch(final String username) {
        return playerRegistry.hasPlayer(username);
    }
}
