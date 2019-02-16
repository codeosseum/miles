package com.codeosseum.miles.faultseeding.submission.evaluation;

import com.codeosseum.miles.faultseeding.task.Task;

public interface SubmissionEvaluator {
    EvaluationResult evaluate(String submission) throws EvaluationException;

    Task getEvaluatedTask();
}
