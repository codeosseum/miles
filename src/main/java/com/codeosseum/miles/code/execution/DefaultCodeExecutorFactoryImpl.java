package com.codeosseum.miles.code.execution;

import java.util.ArrayList;
import java.util.List;

import org.graalvm.polyglot.Engine;

public class DefaultCodeExecutorFactoryImpl implements CodeExecutorFactory {
    private final Engine engine;

    private final List<CodeExecutionListener> defaultListeners;

    public DefaultCodeExecutorFactoryImpl(final Engine engine, final List<CodeExecutionListener> defaultListeners) {
        this.engine = engine;
        this.defaultListeners = defaultListeners;
    }

    @Override
    public CodeExecutor makeCodeExecutor(final List<CodeExecutionListener> listeners) {
        final List<CodeExecutionListener> finalListeners = new ArrayList<>();

        finalListeners.addAll(defaultListeners);
        finalListeners.addAll(listeners);

        return new DefaultCodeExecutorImpl(engine, finalListeners);
    }
}
