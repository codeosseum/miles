package com.codeosseum.miles.faultseeding.task.current;

import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.eventbus.dispatch.SignalConsumer;
import com.codeosseum.miles.faultseeding.match.setup.starting.MatchStartingSignal;

public class CurrentTaskStepperListener implements SignalConsumer {
    private final CurrentTaskService currentTaskService;

    private final EventDispatcher eventDispatcher;

    public CurrentTaskStepperListener(final CurrentTaskService currentTaskService, final EventDispatcher eventDispatcher) {
        this.currentTaskService = currentTaskService;
        this.eventDispatcher = eventDispatcher;

        eventDispatcher.registerConsumer(MatchStartingSignal.class, this);
    }

    @Override
    public void accept() {
        currentTaskService.stepCurrentTask();

        eventDispatcher.dispatchEvent(NewTaskSetSignal.class);
    }
}
