package com.codeosseum.miles.heartbeat.configuration;

import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.heartbeat.HeartbeatTicker;
import com.codeosseum.miles.util.inject.MilesModule;
import com.google.inject.Inject;
import com.google.inject.Provider;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.Arrays.asList;

public class HeartbeatModule extends MilesModule {
    @Override
    protected List<Class<?>> requires() {
        return asList(EventDispatcher.class);
    }

    @Override
    protected void configureModule() {
        bind(HeartbeatTicker.class).toProvider(HeartbeatTickerProvider.class).asEagerSingleton();
    }

    private static class HeartbeatTickerProvider implements Provider<HeartbeatTicker> {
        private final EventDispatcher eventDispatcher;

        @Inject
        public HeartbeatTickerProvider(final EventDispatcher eventDispatcher) {
            this.eventDispatcher = eventDispatcher;
        }

        @Override
        public HeartbeatTicker get() {
            final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

            return new HeartbeatTicker(eventDispatcher, executorService);
        }
    }
}
