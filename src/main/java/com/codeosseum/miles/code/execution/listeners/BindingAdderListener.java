package com.codeosseum.miles.code.execution.listeners;

import java.util.Map;

import com.codeosseum.miles.code.execution.CodeExecutionListener;
import org.graalvm.polyglot.Context;

import static java.util.Objects.requireNonNull;

public class BindingAdderListener extends CodeExecutionListener {
    private static final String JAVASCRIPT = "js";

    private final Map<String, Object> bindings;

    public static BindingAdderListener fromBindings(final Map<String, Object> bindings) {
        return new BindingAdderListener(requireNonNull(bindings));
    }

    private BindingAdderListener(final Map<String, Object> bindings) {
        this.bindings = bindings;
    }

    @Override
    public void onContextBuilt(final Context context) {
        bindings.forEach((key, value) -> context.getBindings(JAVASCRIPT).putMember(key, value));
    }
}
