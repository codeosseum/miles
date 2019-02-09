package com.codeosseum.miles.player.event;

import com.codeosseum.miles.eventbus.dispatch.EventConsumer;
import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.player.PresentPlayerRegistry;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerJoinedListener implements EventConsumer<PlayerJoinedEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerJoinedListener.class);

    private final PresentPlayerRegistry presentPlayerRegistry;

    @Inject
    public PlayerJoinedListener(final PresentPlayerRegistry presentPlayerRegistry, final EventDispatcher eventDispatcher) {
        this.presentPlayerRegistry = presentPlayerRegistry;
        eventDispatcher.registerConsumer(PlayerJoinedEvent.class, this);
    }

    @Override
    public void accept(final PlayerJoinedEvent playerJoinedEvent) {
        LOGGER.info("Player {} joined", playerJoinedEvent.getId());

        presentPlayerRegistry.addPlayer(playerJoinedEvent.getId());
    }
}
