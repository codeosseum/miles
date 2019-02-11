package com.codeosseum.miles.faultseeding.task.publish;

import com.codeosseum.miles.communication.Message;
import com.codeosseum.miles.communication.push.PushMessageToClientService;
import com.codeosseum.miles.faultseeding.task.Task;
import com.codeosseum.miles.player.PresentPlayerRegistry;

import static com.codeosseum.miles.communication.Message.message;

public class DefaultTaskPublisherImpl implements TaskPublisher {
    private static final String ID_SEPARATOR = "#";

    private static final String NEW_FAULT_SEEDING_TASK = "new-fault-seeding-task";

    private final PresentPlayerRegistry presentPlayerRegistry;

    private final PushMessageToClientService messageService;

    public DefaultTaskPublisherImpl(final PresentPlayerRegistry presentPlayerRegistry, final PushMessageToClientService messageService) {
        this.presentPlayerRegistry = presentPlayerRegistry;
        this.messageService = messageService;
    }

    @Override
    public void publishTask(final Task task) {
        final PublishedTask publishedTask = taskToPublishedTask(task);

        presentPlayerRegistry.getAllPlayers()
                .forEach(userId -> sendTaskToPlayer(userId, publishedTask));
    }

    private PublishedTask taskToPublishedTask(final Task task) {
        return new PublishedTask(makePublishedTaskId(task), task.getTitle(), task.getDescription(), task.getSolutionEntrypoint());
    }

    private String makePublishedTaskId(final Task task) {
        return task.getChallengeId() + ID_SEPARATOR + task.getSolutionId();
    }

    private void sendTaskToPlayer(final String userId, final PublishedTask publishedTask) {
        messageService.sendMessage(userId, message(NEW_FAULT_SEEDING_TASK, publishedTask));
    }
}
