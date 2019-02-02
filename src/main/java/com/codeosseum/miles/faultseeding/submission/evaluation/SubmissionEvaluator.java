package com.codeosseum.miles.faultseeding.submission.evaluation;

public interface SubmissionEvaluator {
    EvaluationResult evaluate(String submission) throws EvaluationException;
}
