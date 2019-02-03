package com.codeosseum.miles.code.output.comparator;

import com.codeosseum.miles.code.execution.CodeExecutor;
import com.codeosseum.miles.code.output.converter.OutputConverter;
import com.google.inject.Inject;

public class JavaOutputComparator implements OutputComparator {
    private final OutputConverter converter;

    @Inject
    public JavaOutputComparator(final OutputConverter converter) {
        this.converter = converter;
    }

    @Override
    public boolean compare(final CodeExecutor originatorExecutor, final Object one, final Object other) {
        return converter.convertToString(one).equals(converter.convertToString(other));
    }
}
