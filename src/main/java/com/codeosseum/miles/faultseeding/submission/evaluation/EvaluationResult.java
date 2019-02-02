package com.codeosseum.miles.faultseeding.submission.evaluation;

import lombok.Value;

import static java.util.Objects.requireNonNull;

@Value
public class EvaluationResult {
    private final Status status;

    private final Exception submissionException;

    private final String actualOutput;

    private final String expectedOutput;

    public static EvaluationResult submissionError(final Exception submissionException) {
        return new EvaluationResult(Status.SUBMISSION_ERROR, requireNonNull(submissionException), null, null);
    }

    public static EvaluationResult evaluatedSubmission(final Status status, final String actualOutput, final String expectedOutput) {
        return new EvaluationResult(requireNonNull(status), null, requireNonNull(actualOutput), requireNonNull(expectedOutput));
    }

    private EvaluationResult(final Status status, final Exception submissionException, final String actualOutput, final String expectedOutput) {
        this.status = status;
        this.submissionException = submissionException;
        this.actualOutput = actualOutput;
        this.expectedOutput = expectedOutput;
    }

    public enum Status {
        SUBMISSION_ERROR, DIFFERENT_ACTUAL_AND_EXPECTED, SAME_ACTUAL_AND_EXPECTED
    }
}
