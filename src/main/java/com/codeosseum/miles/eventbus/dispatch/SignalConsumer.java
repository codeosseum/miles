package com.codeosseum.miles.eventbus.dispatch;

@FunctionalInterface
public interface SignalConsumer {
    void accept();
}
