package com.codeosseum.miles.code.execution.listener.listeners;

import java.util.Timer;
import java.util.TimerTask;

import com.codeosseum.miles.code.execution.listener.CodeExecutionListener;
import com.codeosseum.miles.code.execution.listener.ListenerCodeExecutor;

public class TimeoutListener extends CodeExecutionListener {
    private final Timer timer;

    private long timeoutMilliseconds;

    private boolean executionCancelled;

    public static TimeoutListener withDefaultTimeout(final long timeoutMilliseconds) {
        checkIfTimeoutIsPositive(timeoutMilliseconds);

        return new TimeoutListener(timeoutMilliseconds, new Timer());
    }

    TimeoutListener(final long timeoutMilliseconds, final Timer timer) {
        this.timeoutMilliseconds = timeoutMilliseconds;
        this.timer = timer;
    }

    @Override
    public void onBeforeExecute(final ListenerCodeExecutor.PreExecutionContext preExecutionContext) {
        this.executionCancelled = false;

        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                TimeoutListener.this.executionCancelled = true;

                preExecutionContext.getContext().close(true);
            }
        }, timeoutMilliseconds);
    }

    @Override
    public void onAfterExecute(final ListenerCodeExecutor.PostExecutionContext postExecutionContext) {
        if (executionCancelled) {
            postExecutionContext.setShouldRecreateContext(true);
        } else {
            timer.cancel();
        }
    }

    public long getTimeoutMilliseconds() {
        return timeoutMilliseconds;
    }

    public void setTimeoutMilliseconds(final long timeoutMilliseconds) {
        checkIfTimeoutIsPositive(timeoutMilliseconds);

        this.timeoutMilliseconds = timeoutMilliseconds;
    }

    private static void checkIfTimeoutIsPositive(final long timeoutMilliseconds) {
        if (timeoutMilliseconds <= 0) {
            throw new IllegalArgumentException("The timeout must be greater than zero!");
        }
    }
}
