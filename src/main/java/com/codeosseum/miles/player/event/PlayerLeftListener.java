package com.codeosseum.miles.player.event;

import com.codeosseum.miles.eventbus.dispatch.EventConsumer;
import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.player.PresentPlayerRegistry;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerLeftListener implements EventConsumer<PlayerLeftEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerLeftListener.class);

    private final PresentPlayerRegistry presentPlayerRegistry;

    @Inject
    public PlayerLeftListener(final PresentPlayerRegistry presentPlayerRegistry, final EventDispatcher eventDispatcher) {
        this.presentPlayerRegistry = presentPlayerRegistry;
        eventDispatcher.registerConsumer(PlayerLeftEvent.class, this);
    }

    @Override
    public void accept(final PlayerLeftEvent playerLeftEvent) {
        LOGGER.info("Player {} left", playerLeftEvent.getId());

        presentPlayerRegistry.deletePlayer(playerLeftEvent.getId());
    }
}

