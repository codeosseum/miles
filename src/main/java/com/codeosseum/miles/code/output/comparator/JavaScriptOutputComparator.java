package com.codeosseum.miles.code.output.comparator;

import com.codeosseum.miles.code.execution.CodeExecutor;
import com.google.inject.Inject;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

public class JavaScriptOutputComparator implements OutputComparator {
    private static final String JAVASCRIPT = "js";

    private final Source checkerSource;

    @Inject
    public JavaScriptOutputComparator(final String checkerCode) {
        this.checkerSource = Source.create(JAVASCRIPT, checkerCode);
    }

    @Override
    public boolean compare(final CodeExecutor originatorExecutor, final Object one, final Object other) throws ComparingFailedException {
        try {
            final Value checkerFunction = originatorExecutor.execute(checkerSource);

            return checkerFunction.execute(one, other).asBoolean();
        } catch (final Exception e) {
            throw new ComparingFailedException("Could not compare outputs in JavaScript.", e);
        }
    }
}
