package com.codeosseum.miles.faultseeding.submission;

import lombok.Value;

@Value
public class SubmissionEvaluatedEvent {
    private final SubmissionResult submissionResult;
}
