package com.codeosseum.miles.faultseeding.task.publish;

import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.eventbus.dispatch.SignalConsumer;
import com.codeosseum.miles.faultseeding.task.Task;
import com.codeosseum.miles.faultseeding.task.current.CurrentTaskService;
import com.codeosseum.miles.faultseeding.task.current.NewTaskSetEvent;
import com.google.inject.Inject;

public class TaskPublisherListener implements SignalConsumer {
    private final TaskPublisher taskPublisher;

    private final CurrentTaskService taskService;

    @Inject
    public TaskPublisherListener(final TaskPublisher taskPublisher, final EventDispatcher eventDispatcher, final CurrentTaskService taskService) {
        this.taskPublisher = taskPublisher;
        this.taskService = taskService;

        eventDispatcher.registerConsumer(NewTaskSetEvent.class, this);
    }

    @Override
    public void accept() {
        final Task currentTask = taskService.getCurrentTask();

        taskPublisher.publishTask(currentTask);
    }
}
