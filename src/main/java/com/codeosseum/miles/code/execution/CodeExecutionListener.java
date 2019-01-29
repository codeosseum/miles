package com.codeosseum.miles.code.execution;

import org.graalvm.polyglot.Context;

public abstract class CodeExecutionListener {
    public void onContextSetup(final Context.Builder builder) {

    }

    public void onContextCreated(final Context context) {

    }

    public void onBeforeExecute(final ListenerCodeExecutor.PreExecutionContext preExecutionContext) {

    }

    public void onAfterExecute(final ListenerCodeExecutor.PostExecutionContext postExecutionContext) {

    }
}
