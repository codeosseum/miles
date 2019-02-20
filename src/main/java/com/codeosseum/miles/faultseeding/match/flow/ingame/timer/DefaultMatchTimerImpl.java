package com.codeosseum.miles.faultseeding.match.flow.ingame.timer;

import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.faultseeding.match.Constants;
import com.codeosseum.miles.faultseeding.match.flow.cleanup.MatchOverSignal;
import com.codeosseum.miles.match.MatchStatus;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class DefaultMatchTimerImpl implements MatchTimer {
    // TODO: Read from configuration.
    private static final long COUNTDOWN_MILLISECONDS = 30000;

    private final MatchStatus matchStatus;

    private final EventDispatcher eventDispatcher;

    private final Timer timer;

    private LocalDateTime startedAt;

    @Inject
    public DefaultMatchTimerImpl(final MatchStatus matchStatus, final EventDispatcher eventDispatcher, @Named("match-timer-timer") final Timer timer) {
        this.matchStatus = matchStatus;
        this.eventDispatcher = eventDispatcher;
        this.timer = timer;
    }

    @Override
    public void start() {
        this.startedAt = LocalDateTime.now();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                matchStatus.setCurrentStage(Constants.Stage.CLEANUP);

                eventDispatcher.dispatchEvent(new MatchOverSignal());
            }
        }, COUNTDOWN_MILLISECONDS);
    }

    @Override
    public LocalDateTime startedAt() {
        return this.startedAt;
    }
}
