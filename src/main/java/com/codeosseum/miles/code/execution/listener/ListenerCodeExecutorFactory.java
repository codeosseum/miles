package com.codeosseum.miles.code.execution.listener;

import java.util.ArrayList;
import java.util.List;

import org.graalvm.polyglot.Engine;

import static java.util.Objects.requireNonNull;

public class ListenerCodeExecutorFactory {
    private final Engine engine;

    private final List<CodeExecutionListener> defaultListeners;

    public ListenerCodeExecutorFactory(final Engine engine, final List<CodeExecutionListener> defaultListeners) {
        this.engine = engine;
        this.defaultListeners = defaultListeners;
    }

    public ListenerCodeExecutor makeCodeExecutor(final List<CodeExecutionListener> listeners) {
        final List<CodeExecutionListener> finalListeners = new ArrayList<>();

        finalListeners.addAll(defaultListeners);
        finalListeners.addAll(requireNonNull(listeners));

        return new ListenerCodeExecutor(engine, finalListeners);
    }
}
