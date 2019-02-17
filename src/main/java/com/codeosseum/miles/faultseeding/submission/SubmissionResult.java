package com.codeosseum.miles.faultseeding.submission;

import com.codeosseum.miles.faultseeding.submission.evaluation.EvaluationResult;
import lombok.Value;

import static java.util.Objects.requireNonNull;

@Value
public class SubmissionResult {
    private final Status status;

    private final Submission submission;

    private final EvaluationResult evaluationResult;

    private final Exception evaluationException;

    public static SubmissionResult invalidTask(final Submission submission) {
        return new SubmissionResult(Status.INVALID_TASK, submission, null, null);
    }

    public static SubmissionResult evaluationError(final Submission submission, final Exception evaluationException) {
        return new SubmissionResult(Status.EVALUATION_ERROR, submission, null, requireNonNull(evaluationException));
    }

    public static SubmissionResult successfulEvaluation(final Submission submission, final EvaluationResult evaluationResult) {
        return new SubmissionResult(Status.SUCCESSFUL_EVALUATION, submission, requireNonNull(evaluationResult), null);
    }

    private SubmissionResult(final Status status, final Submission submission, final EvaluationResult evaluationResult, final Exception evaluationException) {
        this.status = requireNonNull(status);
        this.submission = requireNonNull(submission);
        this.evaluationResult = evaluationResult;
        this.evaluationException = evaluationException;
    }

    public enum Status {
        INVALID_TASK,
        EVALUATION_ERROR,
        SUCCESSFUL_EVALUATION
    }
}
