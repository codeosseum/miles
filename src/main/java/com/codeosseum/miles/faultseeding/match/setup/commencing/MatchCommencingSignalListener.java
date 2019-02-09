package com.codeosseum.miles.faultseeding.match.setup.commencing;

import java.util.Timer;

import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.eventbus.dispatch.SignalConsumer;
import com.google.inject.Inject;

public class MatchCommencingSignalListener implements SignalConsumer {
    private final EventDispatcher eventDispatcher;

    @Inject
    public MatchCommencingSignalListener(final EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;

        eventDispatcher.registerConsumer(MatchCommencingSignal.class, this);
    }

    @Override
    public void accept() {
        final MatchStartingCountdown countdown = new MatchStartingCountdown(eventDispatcher, new Timer());

        countdown.start();
    }
}
