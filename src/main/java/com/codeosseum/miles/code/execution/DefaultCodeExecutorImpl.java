package com.codeosseum.miles.code.execution;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

import static java.util.Objects.nonNull;

public class DefaultCodeExecutorImpl implements CodeExecutor {
    private static final String JAVASCRIPT = "js";

    private final List<CodeExecutionListener> listeners;

    private final Engine engine;

    private final Context context;

    public DefaultCodeExecutorImpl(final Engine engine, final List<CodeExecutionListener> listeners) {
        this.engine = engine;
        this.listeners = listeners;
        this.context = makeContext(engine, listeners);
    }

    @Override
    public ExecutionResult execute(final Source source) throws CodeExecutionException {
        Source actualSource = null;
        try {
            actualSource = runOnBeforeExecutionListeners(Objects.requireNonNull(source));

            final Value originalValue = context.eval(actualSource);

            final Value actualValue = runOnExecutionSuccessfulListeners(actualSource, originalValue);

            return new ExecutionResult(actualValue);
        } catch (final Exception e) {
            // TODO: Better exception handling.
            // Exceptions thrown from here will have to reach the user somehow. We have to ease the conversion
            // between these exceptions and their presentation on the user's side.
            runOnExecutionFailedListeners(actualSource, e);

            throw new CodeExecutionException(e);
        }
    }

    @Override
    public void close() throws Exception {
        if (nonNull(context)) {
            context.close();
        }
    }

    private Context makeContext(final Engine engine, final List<CodeExecutionListener> listeners) {
        final Context.Builder builder = Context.newBuilder(JAVASCRIPT);

        builder.engine(engine);

        listeners.forEach(listener -> listener.onContextBuild(builder));

        return builder.build();
    }

    private Value runOnExecutionSuccessfulListeners(final Source source, final Value originalValue) {
        Value result = originalValue;

        for (final CodeExecutionListener listener : listeners) {
            final Optional<Value> alteredValueOptional = listener.onExecutionSuccessful(source, context, originalValue);

            if (alteredValueOptional.isPresent()) {
                result = alteredValueOptional.get();
            }
        }

        return result;
    }

    private void runOnExecutionFailedListeners(final Source source, final Exception originalException) throws CodeExecutionException {
        try {
            listeners.forEach(listener -> listener.onExecutionFailed(source, context, originalException));
        } catch (final Exception e) {
            throw new CodeExecutionException(e);
        }
    }

    private Source runOnBeforeExecutionListeners(final Source originalSource) {
        Source result = originalSource;

        for (final CodeExecutionListener listener : listeners) {
            final Optional<Source> alteredSourceOptional = listener.onBeforeExecute(result, context);

            if (alteredSourceOptional.isPresent()) {
                result = alteredSourceOptional.get();
            }
        }

        return result;
    }
}
