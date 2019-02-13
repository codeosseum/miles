package com.codeosseum.miles.faultseeding.match.setup.waiting;

import com.codeosseum.miles.eventbus.dispatch.EventConsumer;
import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.faultseeding.match.configuration.MatchConfigurationHolder;
import com.codeosseum.miles.player.PresentPlayerRegistry;
import com.codeosseum.miles.player.event.AllPlayersAvailableSignal;
import com.codeosseum.miles.player.event.PlayerJoinedEvent;
import com.google.inject.Inject;

public class PlayerJoinedListener implements EventConsumer<PlayerJoinedEvent> {
    private final MatchConfigurationHolder matchConfigurationHolder;

    private final EventDispatcher eventDispatcher;

    private final PresentPlayerRegistry playerRegistry;

    @Inject
    public PlayerJoinedListener(final MatchConfigurationHolder matchConfigurationHolder, final EventDispatcher eventDispatcher, final PresentPlayerRegistry playerRegistry) {
        this.matchConfigurationHolder = matchConfigurationHolder;
        this.eventDispatcher = eventDispatcher;
        this.playerRegistry = playerRegistry;

        eventDispatcher.registerConsumer(PlayerJoinedEvent.class, this);
    }

    @Override
    public void accept(final PlayerJoinedEvent playerJoinedEvent) {
        if (allPlayersArePresent()) {
            eventDispatcher.dispatchEvent(new AllPlayersAvailableSignal());
        }
    }

    private boolean allPlayersArePresent() {
        final int expectedPlayerCount = matchConfigurationHolder.get().getPlayers().size();
        final int actualPlayerCount = playerRegistry.getAllPlayers().size();

        final int missingPlayerCount = expectedPlayerCount - actualPlayerCount;

        // missingPlayerCount == 1 is OK, because this listener might get called
        // before the most recently joined player gets added to the registry.
        return missingPlayerCount <= 1;
    }


}
