package com.codeosseum.miles.heartbeat;

import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.registration.ServerRegisteredSignal;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.concurrent.TimeUnit.SECONDS;

public class HeartbeatTicker {
    private final EventDispatcher eventDispatcher;

    private final AtomicBoolean shouldTick;

    public HeartbeatTicker(final EventDispatcher eventDispatcher, final ScheduledExecutorService executorService) {
        this.eventDispatcher = eventDispatcher;

        this.shouldTick = new AtomicBoolean(false);

        eventDispatcher.registerConsumer(ServerRegisteredSignal.class, this::serverRegisteredSignalConsumer);

        executorService.scheduleAtFixedRate(this::publishHeartbeatSignal, 0L, 30L, SECONDS);
    }

    private void serverRegisteredSignalConsumer(final ServerRegisteredSignal signal) {
        this.shouldTick.set(true);
    }

    private void publishHeartbeatSignal() {
        if (shouldTick.get()) {
            eventDispatcher.dispatchEvent(new HeartbeatSignal());
        }
    }
}
