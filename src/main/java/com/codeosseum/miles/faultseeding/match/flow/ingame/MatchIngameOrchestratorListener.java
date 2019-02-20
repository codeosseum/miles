package com.codeosseum.miles.faultseeding.match.flow.ingame;

import java.util.Map;

import com.codeosseum.miles.communication.Message;
import com.codeosseum.miles.communication.push.PushMessageToClientService;
import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.faultseeding.match.flow.setup.starting.MatchStartingSignal;
import com.codeosseum.miles.faultseeding.scoring.ScoringService;
import com.codeosseum.miles.faultseeding.submission.Submission;
import com.codeosseum.miles.faultseeding.submission.SubmissionEvaluatedEvent;
import com.codeosseum.miles.faultseeding.submission.SubmissionResult;
import com.codeosseum.miles.faultseeding.submission.evaluation.EvaluationResult;
import com.codeosseum.miles.faultseeding.task.Task;
import com.codeosseum.miles.faultseeding.task.current.CurrentTaskService;
import com.codeosseum.miles.faultseeding.task.current.CurrentTaskSetEvent;
import com.codeosseum.miles.player.PresentPlayerRegistry;
import com.google.inject.Inject;
import lombok.Value;

public class MatchIngameOrchestratorListener {
    private final ScoringService scoringService;

    private final CurrentTaskService currentTaskService;

    private final PushMessageToClientService messagingService;

    private final PresentPlayerRegistry playerRegistry;

    private final EventDispatcher eventDispatcher;

    @Inject
    public MatchIngameOrchestratorListener(final ScoringService scoringService, final CurrentTaskService currentTaskService,
                                           final PushMessageToClientService messagingService, final PresentPlayerRegistry playerRegistry,
                                           final EventDispatcher eventDispatcher) {
        this.scoringService = scoringService;
        this.currentTaskService = currentTaskService;
        this.messagingService = messagingService;
        this.playerRegistry = playerRegistry;
        this.eventDispatcher = eventDispatcher;

        eventDispatcher.registerConsumer(SubmissionEvaluatedEvent.class, this::onSubmissionEvaluated);
        eventDispatcher.registerConsumer(MatchStartingSignal.class, this::onMatchStarting);
    }

    void onSubmissionEvaluated(final SubmissionEvaluatedEvent event) {
        final SubmissionResult result = event.getSubmissionResult();

        if (isInvalidTask(result)) {
            sendInvalidTaskMessage(result.getSubmission());
        } else if (isEvaluationError(result)) {
            sendEvaluationErrorMessage(result.getSubmission());
        } else if (isErroneousSubmission(result)) {
            sendErroneousSubmissionMessage(result.getSubmission(), result.getEvaluationResult().getSubmissionError());
        } else if (noBugFound(result)) {
            sendIncorrectSubmissionMessage(result.getSubmission(), result.getEvaluationResult().getActualOutput());
        } else {
            tryStepFlow(result);
        }
    }

    void onMatchStarting() {
        stepCurrentTask();

        sendMatchStartingStep();
    }

    private boolean isInvalidTask(final SubmissionResult result) {
        return result.getStatus().equals(SubmissionResult.Status.INVALID_TASK);
    }

    private void sendInvalidTaskMessage(final Submission submission) {
        final InvalidTaskPayload payload = new InvalidTaskPayload(submission.getId(), submission.getTaskId());

        final Message<InvalidTaskPayload> message = Message.message(InvalidTaskPayload.ACTION, payload);

        messagingService.sendMessage(submission.getUserId(), message);
    }

    private boolean isEvaluationError(final SubmissionResult result) {
        return result.getStatus().equals(SubmissionResult.Status.EVALUATION_ERROR);
    }

    private void sendEvaluationErrorMessage(final Submission submission) {
        final EvaluationErrorPayload payload = new EvaluationErrorPayload(submission.getId(), submission.getTaskId());

        final Message<EvaluationErrorPayload> message = Message.message(EvaluationErrorPayload.ACTION, payload);

        messagingService.sendMessage(submission.getUserId(), message);
    }

    private boolean isErroneousSubmission(final SubmissionResult result) {
        return result.getEvaluationResult().getStatus().equals(EvaluationResult.Status.SUBMISSION_ERROR);
    }

    private void sendErroneousSubmissionMessage(final Submission submission, final String output) {
        final ErroneousSubmissionPayload payload = new ErroneousSubmissionPayload(submission.getId(), submission.getTaskId(), output);

        final Message<ErroneousSubmissionPayload> message = Message.message(ErroneousSubmissionPayload.ACTION, payload);

        messagingService.sendMessage(submission.getUserId(), message);
    }

    private boolean noBugFound(final SubmissionResult result) {
        return result.getEvaluationResult().getStatus().equals(EvaluationResult.Status.SAME_ACTUAL_AND_EXPECTED);
    }

    private void sendIncorrectSubmissionMessage(final Submission submission, final String output) {
        final IncorrectSubmissionPayload payload = new IncorrectSubmissionPayload(submission.getId(), submission.getTaskId(), output);

        final Message<IncorrectSubmissionPayload> incorrectSubmissionMessage = Message.message(IncorrectSubmissionPayload.ACTION, payload);

        messagingService.sendMessage(submission.getUserId(), incorrectSubmissionMessage);
    }

    private void tryStepFlow(final SubmissionResult result) {
        try {
            stepCurrentTask();

            scoringService.scoreSubmission(result);

            sendIngameStep(result);
        } catch(final Exception e) {
            sendInvalidTaskMessage(result.getSubmission());
        }
    }

    private void stepCurrentTask() {
        currentTaskService.stepCurrentTask();

        final Task currentTask = currentTaskService.getCurrentTask();

        eventDispatcher.dispatchEvent(new CurrentTaskSetEvent(currentTask));
    }

    private void sendIngameStep(final SubmissionResult result) {
        final TaskStepPayload payload = new TaskStepPayload(makePreviousTask(result.getSubmission()), makeNextTask(), scoringService.getScores());

        final Message<TaskStepPayload> taskStepMessage = Message.message(TaskStepPayload.ACTION, payload);

        broadcastMessage(taskStepMessage);
    }

    private void sendMatchStartingStep() {
        final TaskStepPayload payload = new TaskStepPayload(null, makeNextTask(), scoringService.getScores());

        final Message<TaskStepPayload> taskStepMessage = Message.message(TaskStepPayload.ACTION, payload);

        broadcastMessage(taskStepMessage);
    }

    private PreviousTask makePreviousTask(final Submission submission) {
        return new PreviousTask(submission.getId(), submission.getTaskId(), submission.getUserId());
    }

    private NextTask makeNextTask() {
        final Task task = currentTaskService.getCurrentTask();

        return new NextTask(task.getId(), task.getTitle(), task.getDescription(), task.getSolutionEntrypoint());
    }

    private <T> void broadcastMessage(final Message<T> message) {
        playerRegistry.getAllPlayers()
                .forEach(username -> messagingService.sendMessage(username, message));
    }

    @Value
    private static final class InvalidTaskPayload {
        private static final String ACTION = "fault-seeding-invalid-task";

        private final String submissionId;

        private final String taskId;
    }

    @Value
    private static final class EvaluationErrorPayload {
        private static final String ACTION = "fault-seeding-evaluation-error";

        private final String submissionId;

        private final String taskId;
    }

    @Value
    private static final class ErroneousSubmissionPayload {
        private static final String ACTION = "fault-seeding-erroneous-submission";

        private final String submissionId;

        private final String taskId;

        private final String output;
    }

    @Value
    private static final class IncorrectSubmissionPayload {
        private static final String ACTION = "fault-seeding-incorrect-submission";

        private final String submissionId;

        private final String taskId;

        private final String output;
    }

    @Value
    private static final class TaskStepPayload {
        private static final String ACTION = "fault-seeding-task-step";

        private final PreviousTask previousTask;

        private final NextTask nextTask;

        private final Map<String, Integer> scores;
    }

    @Value
    private static final class PreviousTask {
        private final String correctSubmissionId;

        private final String taskId;

        private final String correctPlayerId;
    }

    @Value
    private static class NextTask {
        private final String id;

        private final String title;

        private final String description;

        private final String code;
    }
}
