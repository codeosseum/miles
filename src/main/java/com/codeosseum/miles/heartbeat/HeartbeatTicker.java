package com.codeosseum.miles.heartbeat;

import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;

import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

public class HeartbeatTicker {
    private final EventDispatcher eventDispatcher;

    public HeartbeatTicker(final EventDispatcher eventDispatcher, final ScheduledExecutorService executorService) {
        this.eventDispatcher = eventDispatcher;

        executorService.scheduleAtFixedRate(this::publishHeartbeatSignal, 0L, 30L, SECONDS);
    }

    private void publishHeartbeatSignal() {
        eventDispatcher.dispatchEvent(new HeartbeatSignal());
    }
}
