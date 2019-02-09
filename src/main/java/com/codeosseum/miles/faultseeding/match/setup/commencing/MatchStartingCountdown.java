package com.codeosseum.miles.faultseeding.match.setup.commencing;

import java.util.Timer;
import java.util.TimerTask;

import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.faultseeding.match.setup.starting.MatchStartingSignal;

public class MatchStartingCountdown {
    // TODO: Read from configuration.
    private static final long COUNTDOWN_MILLISECONDS = 5000;

    private final EventDispatcher eventDispatcher;

    private final Timer timer;

    public MatchStartingCountdown(final EventDispatcher eventDispatcher, final Timer timer) {
        this.eventDispatcher = eventDispatcher;
        this.timer = timer;
    }

    public void start() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                eventDispatcher.dispatchEvent(new MatchStartingSignal());
            }
        }, COUNTDOWN_MILLISECONDS);
    }
}
