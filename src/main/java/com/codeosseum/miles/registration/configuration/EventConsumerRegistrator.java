package com.codeosseum.miles.registration.configuration;

import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.registration.listener.StartupListener;
import com.codeosseum.miles.util.inject.initialization.PostConstruct;
import com.google.inject.Inject;

public class EventConsumerRegistrator {
    private final EventDispatcher eventDispatcher;

    private final StartupListener startupListener;

    @Inject
    public EventConsumerRegistrator(final EventDispatcher eventDispatcher, final StartupListener startupListener) {
        this.eventDispatcher = eventDispatcher;
        this.startupListener = startupListener;
    }

    @PostConstruct
    public void postConstruct() {
        eventDispatcher.registerConsumer(startupListener);
    }
}
