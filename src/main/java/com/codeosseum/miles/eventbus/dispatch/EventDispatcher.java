package com.codeosseum.miles.eventbus.dispatch;

public interface EventDispatcher {
    void dispatchEvent(Object event) throws EventDispatchFailedException;

    <E> void registerConsumer(final Class<E> eventType, final EventConsumer<E> consumer);
}

