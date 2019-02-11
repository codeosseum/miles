package com.codeosseum.miles.faultseeding.submission;

import lombok.Value;

@Value
public class SubmissionReceivedEvent {
    private final Submission submission;
}
