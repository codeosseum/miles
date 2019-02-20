package com.codeosseum.miles.faultseeding.match.flow.cleanup;

import com.codeosseum.miles.communication.websocket.session.SessionRegistry;
import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.eventbus.dispatch.SignalConsumer;
import com.codeosseum.miles.player.PresentPlayerRegistry;
import com.codeosseum.miles.player.RegisteredPlayerRegistry;
import com.google.inject.Inject;
import org.eclipse.jetty.websocket.api.Session;

public class ClientCloseListener implements SignalConsumer {
    private final SessionRegistry sessionRegistry;

    private final PresentPlayerRegistry playerRegistry;

    private final RegisteredPlayerRegistry registeredPlayerRegistry;

    @Inject
    public ClientCloseListener(final SessionRegistry sessionRegistry, final PresentPlayerRegistry playerRegistry,
                               final RegisteredPlayerRegistry registeredPlayerRegistry, final EventDispatcher eventDispatcher) {
        this.sessionRegistry = sessionRegistry;
        this.playerRegistry = playerRegistry;
        this.registeredPlayerRegistry = registeredPlayerRegistry;

        eventDispatcher.registerConsumer(ScoreSentToClientsSignal.class, this);
    }

    @Override
    public void accept() {
        sessionRegistry.getActiveSessions()
                .forEach(Session::close);

        registeredPlayerRegistry.deleteAllPlayers();

        playerRegistry.deleteAllPlayers();
    }
}
