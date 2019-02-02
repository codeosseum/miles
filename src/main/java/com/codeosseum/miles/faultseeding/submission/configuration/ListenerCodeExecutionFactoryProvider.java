package com.codeosseum.miles.faultseeding.submission.configuration;

import java.util.List;

import com.codeosseum.miles.code.execution.listener.CodeExecutionListener;
import com.codeosseum.miles.code.execution.listener.ListenerCodeExecutorFactory;
import com.google.inject.Provider;
import org.graalvm.polyglot.Engine;

import static java.util.Collections.*;

public class ListenerCodeExecutionFactoryProvider implements Provider<ListenerCodeExecutorFactory> {
    private final Engine engine;

    private final List<CodeExecutionListener> defaultListeners;

    public ListenerCodeExecutionFactoryProvider() {
        this.engine = Engine.newBuilder().build();
        this.defaultListeners = emptyList();
    }

    @Override
    public ListenerCodeExecutorFactory get() {
        return new ListenerCodeExecutorFactory(engine, defaultListeners);
    }
}
