package com.codeosseum.miles.faultseeding.task.repository;

import com.codeosseum.miles.challenge.repository.ChallengesLoadedSignal;
import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.eventbus.dispatch.SignalConsumer;
import com.codeosseum.miles.faultseeding.submission.evaluation.SubmissionEvaluatorFactory;
import com.google.inject.Inject;

public class ChallengesLoadedListener implements SignalConsumer {
    private final DefaultTaskRepositoryImpl taskRepository;

    private final SubmissionEvaluatorFactory evaluatorFactory;

    @Inject
    public ChallengesLoadedListener(final EventDispatcher eventDispatcher, final DefaultTaskRepositoryImpl taskRepository, SubmissionEvaluatorFactory evaluatorFactory) {
        this.taskRepository = taskRepository;
        this.evaluatorFactory = evaluatorFactory;
        eventDispatcher.registerConsumer(ChallengesLoadedSignal.class, this);
    }

    @Override
    public void accept() {
        taskRepository.reloadTasks();
    }
}
