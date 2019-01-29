package com.codeosseum.miles.code.execution;

import org.graalvm.polyglot.Source;

public interface CodeExecutor extends AutoCloseable {
    ExecutionResult execute(Source source) throws CodeExecutionException;
}
