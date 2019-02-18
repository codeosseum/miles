package com.codeosseum.miles.faultseeding.match.setup.commencing;

import java.util.Timer;

import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.eventbus.dispatch.SignalConsumer;
import com.codeosseum.miles.match.MatchStatus;
import com.google.inject.Inject;

public class MatchCommencingSignalListener implements SignalConsumer {
    private final MatchStatus matchStatus;

    private final EventDispatcher eventDispatcher;

    @Inject
    public MatchCommencingSignalListener(final MatchStatus matchStatus, final EventDispatcher eventDispatcher) {
        this.matchStatus = matchStatus;
        this.eventDispatcher = eventDispatcher;

        eventDispatcher.registerConsumer(MatchCommencingSignal.class, this);
    }

    @Override
    public void accept() {
        final MatchStartingCountdown countdown = new MatchStartingCountdown(eventDispatcher, matchStatus, new Timer());

        countdown.start();
    }
}
