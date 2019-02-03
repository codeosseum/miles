package com.codeosseum.miles.code.output.comparator;

import com.codeosseum.miles.code.execution.CodeExecutor;

public interface OutputComparator {
    boolean compare(CodeExecutor originatorExecutor, Object one, Object other) throws ComparingFailedException;
}
