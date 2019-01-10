package com.codeosseum.miles.eventbus.configuration;

import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.eventbus.dispatch.GuavaEventDispatcherImpl;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EventBusModule extends AbstractModule {
    private static final int EXECUTOR_THREAD_COUNT = 2;

    @Override
    protected void configure() {
        bind(EventDispatcher.class).toInstance(new GuavaEventDispatcherImpl(eventBus()));
    }

    private EventBus eventBus() {
        return new AsyncEventBus(eventBusExecutor());
    }

    private Executor eventBusExecutor() {
        return Executors.newFixedThreadPool(EXECUTOR_THREAD_COUNT);
    }
}
