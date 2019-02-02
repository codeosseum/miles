package com.codeosseum.miles.code.execution.listener;

import java.util.ArrayList;
import java.util.List;

import com.codeosseum.miles.code.execution.CodeExecutionException;
import com.codeosseum.miles.code.execution.CodeExecutor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

import static java.util.Objects.nonNull;

public class ListenerCodeExecutor implements CodeExecutor {
    private static final String JAVASCRIPT = "js";

    private final List<CodeExecutionListener> listeners;

    private final Engine engine;

    private Context context;

    public ListenerCodeExecutor(final Engine engine, final List<CodeExecutionListener> listeners) {
        this.engine = engine;
        this.listeners = new ArrayList<>(listeners);
        this.context = makeContext(engine, listeners);
    }

    @Override
    public Value execute(final Source source) throws CodeExecutionException {
        final PreExecutionContext preExecutionContext = new PreExecutionContext(context, source);

        beforeExecute(preExecutionContext);

        final PostExecutionContext postExecutionContext = doExecute(preExecutionContext);

        afterExecute(postExecutionContext);

        return postExecutionContext.getValue();
    }

    public Value executeWithListeners(final Source source, final List<CodeExecutionListener> listeners) throws CodeExecutionException {
        try {
            this.listeners.addAll(listeners);

            return this.execute(source);
        } finally {
            this.listeners.removeAll(listeners);
        }
    }

    @Override
    public void close() throws Exception {
        if (nonNull(context)) {
            context.close();
        }
    }

    private void beforeExecute(final PreExecutionContext preExecutionContext) throws CodeExecutionException {
        try {
            listeners.forEach(listener -> listener.onBeforeExecute(preExecutionContext));
        } catch (final Exception e) {
            e.printStackTrace();

            final PostExecutionContext postExecutionContext = new PostExecutionContext(false, preExecutionContext.source, context);

            afterExecute(postExecutionContext);

            throw new CodeExecutionException(postExecutionContext.exception);
        }
    }

    private PostExecutionContext doExecute(final PreExecutionContext preExecutionContext) {
        final PostExecutionContext postExecutionContext = new PostExecutionContext(true, preExecutionContext.source, context);
        try {
            final Value originalValue = context.eval(preExecutionContext.source);
            postExecutionContext.setValue(originalValue);
        } catch (final Exception e) {
            postExecutionContext.setException(e);
        }

        return postExecutionContext;
    }

    private void afterExecute(final PostExecutionContext postExecutionContext) throws CodeExecutionException {
        final boolean shouldThrow = nonNull(postExecutionContext.getException());

        try {
            listeners.forEach(listener -> listener.onAfterExecute(postExecutionContext));

            if (postExecutionContext.shouldRecreateContext) {
                context = makeContext(engine, listeners);
            }
        } catch (final Exception e){
            throw new CodeExecutionException(e);
        }

        if (shouldThrow) {
            throw new CodeExecutionException(postExecutionContext.getException());
        }
    }

    private Context makeContext(final Engine engine, final List<CodeExecutionListener> listeners) {
        final Context.Builder builder = Context.newBuilder(JAVASCRIPT);

        builder.engine(engine);

        listeners.forEach(listener -> listener.onContextSetup(builder));

        final Context context = builder.build();

        listeners.forEach(listener -> listener.onContextCreated(context));

        return context;
    }



    @Data
    @AllArgsConstructor
    public final class PreExecutionContext {
        private final Context context;

        private Source source;
    }

    @Data
    @RequiredArgsConstructor
    public final class PostExecutionContext {
        private final boolean executionRun;

        private final Source source;

        private final Context context;

        private Value value;

        private Exception exception;

        private boolean shouldRecreateContext;
    }
}
