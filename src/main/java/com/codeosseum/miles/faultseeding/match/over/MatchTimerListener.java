package com.codeosseum.miles.faultseeding.match.over;

import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.eventbus.dispatch.SignalConsumer;
import com.codeosseum.miles.faultseeding.match.setup.starting.MatchStartingSignal;
import com.google.inject.Inject;

public class MatchTimerListener implements SignalConsumer {
    private final MatchTimer matchTimer;

    @Inject
    public MatchTimerListener(final MatchTimer matchTimer, final EventDispatcher eventDispatcher) {
        this.matchTimer = matchTimer;

        eventDispatcher.registerConsumer(MatchStartingSignal.class, this);
    }

    @Override
    public void accept() {
        matchTimer.start();
    }
}
