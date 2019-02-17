package com.codeosseum.miles.faultseeding.submission.evaluation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.faultseeding.submission.Submission;
import com.codeosseum.miles.faultseeding.submission.SubmissionEvaluatedEvent;
import com.codeosseum.miles.faultseeding.submission.SubmissionReceivedEvent;
import com.codeosseum.miles.faultseeding.submission.SubmissionResult;
import com.codeosseum.miles.faultseeding.task.current.CurrentTaskSetEvent;
import com.codeosseum.miles.player.RegisteredPlayerRegistry;
import com.google.inject.Inject;

import static com.codeosseum.miles.faultseeding.submission.SubmissionResult.evaluationError;
import static com.codeosseum.miles.faultseeding.submission.SubmissionResult.successfulEvaluation;

public class SubmissionEvaluationService {
    private final RegisteredPlayerRegistry playerRegistry;

    private final SubmissionEvaluatorFactory evaluatorFactory;

    private final EventDispatcher eventDispatcher;

    private final Map<String, SubmissionEvaluator> evaluatorMap;

    @Inject
    public SubmissionEvaluationService(final RegisteredPlayerRegistry playerRegistry, final SubmissionEvaluatorFactory evaluatorFactory,
                                       final EventDispatcher eventDispatcher) {
        this.playerRegistry = playerRegistry;
        this.evaluatorFactory = evaluatorFactory;

        this.eventDispatcher = eventDispatcher;

        this.evaluatorMap = new ConcurrentHashMap<>();

        eventDispatcher.registerConsumer(CurrentTaskSetEvent.class, this::onCurrentTaskSet);

        eventDispatcher.registerConsumer(SubmissionReceivedEvent.class, this::onSubmissionReceived);
    }

    void onCurrentTaskSet(final CurrentTaskSetEvent event) {
        evaluatorMap.clear();

        playerRegistry.getAllPlayers()
                .forEach(username -> {
                    final SubmissionEvaluator evaluator = evaluatorFactory.evaluatorForTask(event.getTask());

                    evaluatorMap.put(username, evaluator);
                });
    }

    void onSubmissionReceived(final SubmissionReceivedEvent event) {
        final Submission submission = event.getSubmission();

        final SubmissionEvaluator evaluator = evaluatorMap.get(submission.getUserId());

        if (/* Task IDs do not match*/ false) {

        }

        final SubmissionResult result = evaluateSubmission(evaluator, submission);

        eventDispatcher.dispatchEvent(new SubmissionEvaluatedEvent(result));
    }

    private SubmissionResult evaluateSubmission(final SubmissionEvaluator evaluator, final Submission submission) {
        try {
            final EvaluationResult evaluationResult = evaluator.evaluate(submission.getCode());

            return successfulEvaluation(submission, evaluationResult);
        } catch (final Exception e) {
            return evaluationError(submission, e);
        }
    }
}
