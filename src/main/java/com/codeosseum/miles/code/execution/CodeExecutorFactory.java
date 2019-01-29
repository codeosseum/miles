package com.codeosseum.miles.code.execution;

import java.util.List;

public interface CodeExecutorFactory {
    CodeExecutor makeCodeExecutor(List<CodeExecutionListener> listeners);
}
