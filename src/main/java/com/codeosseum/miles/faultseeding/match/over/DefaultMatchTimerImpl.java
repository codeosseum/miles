package com.codeosseum.miles.faultseeding.match.over;

import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class DefaultMatchTimerImpl implements MatchTimer {
    // TODO: Read from configuration.
    private static final long COUNTDOWN_MILLISECONDS = 30000;

    private final EventDispatcher eventDispatcher;

    private final Timer timer;

    private LocalDateTime startedAt;

    @Inject
    public DefaultMatchTimerImpl(final EventDispatcher eventDispatcher, @Named("match-timer-timer") final Timer timer) {
        this.eventDispatcher = eventDispatcher;
        this.timer = timer;
    }

    @Override
    public void start() {
        this.startedAt = LocalDateTime.now();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                eventDispatcher.dispatchEvent(new MatchOverSignal());
            }
        }, COUNTDOWN_MILLISECONDS);
    }

    @Override
    public LocalDateTime startedAt() {
        return this.startedAt;
    }
}
