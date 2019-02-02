package com.codeosseum.miles.code.execution.listener.listeners;

import java.util.Map;

import com.codeosseum.miles.code.execution.listener.CodeExecutionListener;
import org.graalvm.polyglot.Context;

import static java.util.Objects.requireNonNull;

public class PermanentBindingAdderListener extends CodeExecutionListener {
    private static final String JAVASCRIPT = "js";

    private final Map<String, Object> bindings;

    public static PermanentBindingAdderListener fromBindings(final Map<String, Object> bindings) {
        return new PermanentBindingAdderListener(requireNonNull(bindings));
    }

    private PermanentBindingAdderListener(final Map<String, Object> bindings) {
        this.bindings = bindings;
    }

    @Override
    public void onContextCreated(final Context context) {
        bindings.forEach((key, value) -> context.getBindings(JAVASCRIPT).putMember(key, value));
    }
}
