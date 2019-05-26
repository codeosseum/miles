package com.codeosseum.miles.faultseeding.match.flow.setup.commencing;

import java.util.Timer;

import com.codeosseum.miles.configuration.FaultSeedingConfig;
import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.eventbus.dispatch.SignalConsumer;
import com.codeosseum.miles.match.MatchStatus;
import com.google.inject.Inject;

public class MatchCommencingSignalListener implements SignalConsumer {
    private final MatchStatus matchStatus;

    private final EventDispatcher eventDispatcher;

    private final FaultSeedingConfig configuration;

    @Inject
    public MatchCommencingSignalListener(final MatchStatus matchStatus, final EventDispatcher eventDispatcher,
                                         final FaultSeedingConfig configuration) {
        this.matchStatus = matchStatus;
        this.eventDispatcher = eventDispatcher;
        this.configuration = configuration;

        eventDispatcher.registerConsumer(MatchCommencingSignal.class, this);
    }

    @Override
    public void accept() {
        final MatchStartingCountdown countdown = new MatchStartingCountdown(eventDispatcher, matchStatus,
                new Timer(), configuration.getStartingCountdownSeconds());

        countdown.start();
    }
}
