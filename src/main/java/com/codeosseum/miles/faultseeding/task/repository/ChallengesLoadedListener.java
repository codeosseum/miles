package com.codeosseum.miles.faultseeding.task.repository;

import com.codeosseum.miles.challenge.repository.ChallengesLoadedSignal;
import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.eventbus.dispatch.SignalConsumer;
import com.google.inject.Inject;

public class ChallengesLoadedListener implements SignalConsumer {
    private final DefaultTaskRepositoryImpl taskRepository;

    @Inject
    public ChallengesLoadedListener(final EventDispatcher eventDispatcher, final DefaultTaskRepositoryImpl taskRepository) {
        this.taskRepository = taskRepository;
        eventDispatcher.registerConsumer(ChallengesLoadedSignal.class, this);


    }

    @Override
    public void accept() {
        taskRepository.reloadTasks();
    }
}
