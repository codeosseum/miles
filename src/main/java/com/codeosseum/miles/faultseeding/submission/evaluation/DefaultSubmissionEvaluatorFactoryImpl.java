package com.codeosseum.miles.faultseeding.submission.evaluation;

import com.codeosseum.miles.code.execution.listener.ListenerCodeExecutorFactory;
import com.codeosseum.miles.faultseeding.task.Task;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;

public class DefaultSubmissionEvaluatorFactoryImpl implements SubmissionEvaluatorFactory {
    private static final String FACTORY_IDENTIFIER = "fault-seeding-submission-evaluator-factory";

    private final ListenerCodeExecutorFactory executorFactory;

    @Inject
    public DefaultSubmissionEvaluatorFactoryImpl(@Named(FACTORY_IDENTIFIER) ListenerCodeExecutorFactory executorFactory) {
        this.executorFactory = executorFactory;
    }

    @Override
    public SubmissionEvaluator evaluatorForTask(final Task task) {
        return new DefaultSubmissionEvaluatorImpl(executorFactory.makeCodeExecutor(emptyList()), requireNonNull(task));
    }
}
