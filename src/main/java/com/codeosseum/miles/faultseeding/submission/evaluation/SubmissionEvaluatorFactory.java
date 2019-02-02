package com.codeosseum.miles.faultseeding.submission.evaluation;

import com.codeosseum.miles.faultseeding.task.Task;

public interface SubmissionEvaluatorFactory {
    SubmissionEvaluator evaluatorForTask(Task task);
}
