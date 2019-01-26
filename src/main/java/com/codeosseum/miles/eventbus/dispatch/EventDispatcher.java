package com.codeosseum.miles.eventbus.dispatch;

import com.codeosseum.miles.eventbus.Signal;

public interface EventDispatcher {
    void dispatchEvent(Object event) throws EventDispatchFailedException;

    <E> void registerConsumer(final Class<E> eventType, final EventConsumer<E> consumer);

    <S extends Signal> void registerConsumer(final Class<S> signalType, SignalConsumer consumer);
}

