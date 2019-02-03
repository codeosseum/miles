package com.codeosseum.miles.code.execution.listener.listeners;

import com.codeosseum.miles.code.execution.listener.CodeExecutionListener;
import com.codeosseum.miles.code.execution.listener.ListenerCodeExecutor;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

import static java.util.Objects.isNull;

public class ModuleExportsListener extends CodeExecutionListener {
    private static final String JAVASCRIPT = "js";

    private static final String MODULE = "module";

    private static final String EXPORTS = "exports";

    private static final String EMPTY_OBJECT_EXPRESSION = "({})";

    private Value moduleBinding;

    @Override
    public void onBeforeExecute(final ListenerCodeExecutor.PreExecutionContext preExecutionContext) {
        moduleBinding = preExecutionContext.getContext().eval(Source.create(JAVASCRIPT, EMPTY_OBJECT_EXPRESSION));

        preExecutionContext.getContext().getBindings(JAVASCRIPT).putMember(MODULE, moduleBinding);
    }

    @Override
    public void onAfterExecute(final ListenerCodeExecutor.PostExecutionContext postExecutionContext) {
        postExecutionContext.getContext().getBindings(JAVASCRIPT).removeMember(MODULE);
    }

    public Value get() {
        if (isNull(moduleBinding)) {
             return null;
        } else {
            return moduleBinding.getMember(EXPORTS);
        }
    }
}
