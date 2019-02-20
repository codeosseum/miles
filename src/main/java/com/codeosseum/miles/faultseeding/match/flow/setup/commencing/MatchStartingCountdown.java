package com.codeosseum.miles.faultseeding.match.flow.setup.commencing;

import java.util.Timer;
import java.util.TimerTask;

import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.faultseeding.match.Constants;
import com.codeosseum.miles.faultseeding.match.flow.setup.starting.MatchStartingSignal;
import com.codeosseum.miles.match.MatchStatus;

public class MatchStartingCountdown {
    // TODO: Read from configuration.
    private static final long COUNTDOWN_MILLISECONDS = 5000;

    private final EventDispatcher eventDispatcher;

    private final MatchStatus matchStatus;

    private final Timer timer;

    public MatchStartingCountdown(final EventDispatcher eventDispatcher, final MatchStatus matchStatus, final Timer timer) {
        this.eventDispatcher = eventDispatcher;
        this.matchStatus = matchStatus;
        this.timer = timer;
    }

    public void start() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                matchStatus.setCurrentStage(Constants.Stage.IN_PROGRESS);

                eventDispatcher.dispatchEvent(new MatchStartingSignal());
            }
        }, COUNTDOWN_MILLISECONDS);
    }
}
