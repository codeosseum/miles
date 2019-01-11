package com.codeosseum.miles.eventbus.dispatch;

import java.util.function.Consumer;

@FunctionalInterface
public interface EventConsumer<E> extends Consumer<E> {
}

