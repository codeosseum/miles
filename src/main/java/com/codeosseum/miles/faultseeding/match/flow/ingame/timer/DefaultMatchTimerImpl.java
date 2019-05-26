package com.codeosseum.miles.faultseeding.match.flow.ingame.timer;

import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

import com.codeosseum.miles.configuration.FaultSeedingConfig;
import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.faultseeding.match.Constants;
import com.codeosseum.miles.faultseeding.match.flow.cleanup.MatchOverSignal;
import com.codeosseum.miles.match.MatchStatus;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class DefaultMatchTimerImpl implements MatchTimer {
    private static final long MILLISECONDS_IN_A_SECOND = 1000;

    private final MatchStatus matchStatus;

    private final EventDispatcher eventDispatcher;

    private final Timer timer;

    private final FaultSeedingConfig configuration;

    private LocalDateTime startedAt;

    @Inject
    public DefaultMatchTimerImpl(final MatchStatus matchStatus, final EventDispatcher eventDispatcher,
                                 @Named("match-timer-timer") final Timer timer, final FaultSeedingConfig configuration) {
        this.matchStatus = matchStatus;
        this.eventDispatcher = eventDispatcher;
        this.timer = timer;
        this.configuration = configuration;
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
        }, configuration.getRuntimeSeconds() * MILLISECONDS_IN_A_SECOND);
    }

    @Override
    public LocalDateTime startedAt() {
        return this.startedAt;
    }
}
