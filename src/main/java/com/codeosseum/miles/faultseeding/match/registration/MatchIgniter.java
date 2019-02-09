package com.codeosseum.miles.faultseeding.match.registration;

import com.codeosseum.miles.eventbus.dispatch.EventConsumer;
import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.faultseeding.match.Constants;
import com.codeosseum.miles.faultseeding.match.configuration.MatchConfigurationHolder;
import com.codeosseum.miles.faultseeding.match.setup.commencing.MatchCommencingSignal;
import com.codeosseum.miles.match.MatchStatus;
import com.codeosseum.miles.player.RegisteredPlayerRegistry;
import com.google.inject.Inject;

public class MatchIgniter implements EventConsumer<FaultSeedingMatchRegisteredEvent> {
    private final MatchStatus matchStatus;

    private final MatchConfigurationHolder matchConfigurationHolder;

    private final RegisteredPlayerRegistry registeredPlayerRegistry;

    private final EventDispatcher eventDispatcher;

    @Inject
    public MatchIgniter(final MatchStatus matchStatus, final MatchConfigurationHolder matchConfigurationHolder,
                        final EventDispatcher eventDispatcher, final RegisteredPlayerRegistry registeredPlayerRegistry) {
        this.matchStatus = matchStatus;
        this.matchConfigurationHolder = matchConfigurationHolder;
        this.registeredPlayerRegistry = registeredPlayerRegistry;
        this.eventDispatcher = eventDispatcher;

        eventDispatcher.registerConsumer(FaultSeedingMatchRegisteredEvent.class, this);
    }

    @Override
    public void accept(final FaultSeedingMatchRegisteredEvent event) {
        matchStatus.setCurrentMode(Constants.MODE);
        matchStatus.setCurrentStage(Constants.Stage.WAITING_FOR_PLAYERS);

        matchConfigurationHolder.set(event.getMatchConfiguration());

        event.getMatchConfiguration().getPlayers().forEach(registeredPlayerRegistry::addPlayer);

        eventDispatcher.dispatchEvent(new MatchCommencingSignal());
    }
}
