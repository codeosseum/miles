package com.codeosseum.miles.code.execution.listener.listeners;

import java.util.List;

import com.codeosseum.miles.code.execution.listener.CodeExecutionListener;
import org.graalvm.polyglot.Context;

import static java.util.Objects.requireNonNull;

public class PermanentBindingRemoverListener extends CodeExecutionListener {
    private static final String JAVASCRIPT = "js";

    private final List<String> bindings;

    public static PermanentBindingRemoverListener fromBindings(List<String> bindings) {
        return new PermanentBindingRemoverListener(requireNonNull(bindings));
    }

    private PermanentBindingRemoverListener(List<String> bindings) {
        this.bindings = bindings;
    }

    @Override
    public void onContextCreated(final Context context) {
        bindings.forEach(name -> context.getBindings(JAVASCRIPT).removeMember(name));
    }
}
