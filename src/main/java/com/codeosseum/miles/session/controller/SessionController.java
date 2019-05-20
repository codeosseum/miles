package com.codeosseum.miles.session.controller;

import com.codeosseum.miles.communication.websocket.controller.JsonWebSocketController;
import com.codeosseum.miles.communication.websocket.dispatcher.WebSocketDispatcher;
import com.codeosseum.miles.communication.websocket.session.SessionRegistry;
import com.codeosseum.miles.communication.websocket.transmission.MessageTransmitter;
import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.match.MatchStatus;
import com.codeosseum.miles.player.PresentPlayerRegistry;
import com.codeosseum.miles.player.RegisteredPlayerRegistry;
import com.codeosseum.miles.player.event.PlayerJoinedEvent;
import com.codeosseum.miles.player.event.PlayerLeftEvent;
import com.google.gson.Gson;
import com.google.inject.Inject;
import org.eclipse.jetty.websocket.api.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionController extends JsonWebSocketController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionController.class);

    private final SessionRegistry sessionRegistry;

    private final RegisteredPlayerRegistry registeredPlayerRegistry;

    private final PresentPlayerRegistry presentPlayerRegistry;

    private final MatchStatus matchStatus;

    private final EventDispatcher eventDispatcher;

    @Inject
    public SessionController(final Gson gson, final MessageTransmitter messageTransmitter, final SessionRegistry sessionRegistry,
                             final RegisteredPlayerRegistry registeredPlayerRegistry, final PresentPlayerRegistry presentPlayerRegistry,
                             final MatchStatus matchStatus, final EventDispatcher eventDispatcher) {
        super(gson, messageTransmitter);

        this.sessionRegistry = sessionRegistry;
        this.registeredPlayerRegistry = registeredPlayerRegistry;
        this.presentPlayerRegistry = presentPlayerRegistry;
        this.matchStatus = matchStatus;
        this.eventDispatcher = eventDispatcher;
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
        final String username = payload.getUsername();
        final String suppliedPassword = payload.getJoinPassword();

        LOGGER.info("User {} trying to authenticate", username);

        final boolean canAuthenticate =
                sessionNotAuthenticated(session)
                && userNotAuthenticated(username)
                && userIsParticipantOfTheMatch(username)
                && passwordsMatch(suppliedPassword);

        if (canAuthenticate) {
            sessionRegistry.addAuthenticatedSession(session, username);

            presentPlayerRegistry.addPlayer(username);

            eventDispatcher.dispatchEvent(new PlayerJoinedEvent(username));
        }
    }

    private boolean passwordsMatch(final String suppliedPassword) {
        return matchStatus.getJoinPassword().equals(suppliedPassword);
    }

    private void onConnectionClose(final Session session, final int statusCode, final String reason) {
        LOGGER.info("WS connection closed at {} with statusCode {} and reason {}", session.getRemoteAddress(), statusCode, reason);

        sessionRegistry.getIdForSession(session)
                .map(PlayerLeftEvent::new)
                .ifPresent(eventDispatcher::dispatchEvent);

        sessionRegistry.removeActiveSession(session);
    }

    private boolean sessionNotAuthenticated(final Session session) {
        return !sessionRegistry.getIdForSession(session).isPresent();
    }

    private boolean userNotAuthenticated(final String username) {
        return !sessionRegistry.getSessionForId(username).isPresent();
    }

    private boolean userIsParticipantOfTheMatch(final String username) {
        return registeredPlayerRegistry.hasPlayer(username);
    }
}
