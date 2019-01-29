package com.codeosseum.miles.code.execution.listeners;

import java.util.List;

import com.codeosseum.miles.code.execution.CodeExecutionListener;
import org.graalvm.polyglot.Context;

import static java.util.Objects.requireNonNull;

public class BindingRemoverListener extends CodeExecutionListener {
    private static final String JAVASCRIPT = "js";

    private final List<String> bindings;

    public static BindingRemoverListener fromBindings(List<String> bindings) {
        return new BindingRemoverListener(requireNonNull(bindings));
    }

    private BindingRemoverListener(List<String> bindings) {
        this.bindings = bindings;
    }

    @Override
    public void onContextCreated(final Context context) {
        bindings.forEach(name -> context.getBindings(JAVASCRIPT).removeMember(name));
    }
}
