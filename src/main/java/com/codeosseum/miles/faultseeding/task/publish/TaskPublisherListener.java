package com.codeosseum.miles.faultseeding.task.publish;

import com.codeosseum.miles.eventbus.dispatch.EventConsumer;
import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.faultseeding.task.current.NewTaskSetEvent;
import com.google.inject.Inject;

public class TaskPublisherListener implements EventConsumer<NewTaskSetEvent> {
    private final TaskPublisher taskPublisher;

    @Inject
    public TaskPublisherListener(final TaskPublisher taskPublisher, final EventDispatcher eventDispatcher) {
        this.taskPublisher = taskPublisher;

        eventDispatcher.registerConsumer(NewTaskSetEvent.class, this);
    }

    @Override
    public void accept(final NewTaskSetEvent event) {
        taskPublisher.publishTask(event.getTask());
    }
}
