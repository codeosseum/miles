package com.codeosseum.miles.faultseeding.task;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Task {
    private final String challengeId;

    private final String solutionId;

    private final int difficulty;

    private final String title;

    private final String description;

    private final String evaluatorEntrypoint;

    private final String solutionEntrypoint;
}
