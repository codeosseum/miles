package com.codeosseum.miles.code.execution;

import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

public interface CodeExecutor extends AutoCloseable {
    Value execute(Source source) throws CodeExecutionException;
}
