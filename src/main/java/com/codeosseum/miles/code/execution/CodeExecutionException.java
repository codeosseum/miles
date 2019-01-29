package com.codeosseum.miles.code.execution;

public class CodeExecutionException extends Exception {
    public CodeExecutionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public CodeExecutionException(final Throwable cause) {
        super(cause);
    }
}
