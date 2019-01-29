package com.codeosseum.miles.code.execution;

import java.util.Optional;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

public abstract class CodeExecutionListener {
    public void onContextBuild(final Context.Builder builder) {

    }

    public void onContextBuilt(final Context context) {

    }

    public Optional<Source> onBeforeExecute(final Source source, final Context context) {
        return Optional.empty();
    }

    public Optional<Value> onExecutionSuccessful(final Source source, final Context context, final Value originalValue) {
        return Optional.empty();
    }

    public void onExecutionFailed(final Source source, final Context context, final Exception originalException) {

    }
}
