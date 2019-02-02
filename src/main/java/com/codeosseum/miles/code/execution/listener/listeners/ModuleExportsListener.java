package com.codeosseum.miles.code.execution.listener.listeners;

import com.codeosseum.miles.code.execution.listener.CodeExecutionListener;
import com.codeosseum.miles.code.execution.listener.ListenerCodeExecutor;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

import static java.util.Objects.isNull;

public class ModuleExportsListener extends CodeExecutionListener {
    private static final String JAVASCRIPT = "js";

    private Value moduleBinding;

    @Override
    public void onBeforeExecute(final ListenerCodeExecutor.PreExecutionContext preExecutionContext) {
        moduleBinding = preExecutionContext.getContext().eval(Source.create(JAVASCRIPT, "({})"));

        preExecutionContext.getContext().getBindings(JAVASCRIPT).putMember("module", moduleBinding);
    }

    @Override
    public void onAfterExecute(final ListenerCodeExecutor.PostExecutionContext postExecutionContext) {
        System.out.println(moduleBinding);
        postExecutionContext.getContext().getBindings(JAVASCRIPT).removeMember("module");
    }

    public Value get() {
        if (isNull(moduleBinding)) {
             return null;
        } else {
            return moduleBinding.getMember("exports");
        }
    }
}
