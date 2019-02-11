package com.codeosseum.miles.faultseeding.submission;

import lombok.Value;

@Value
public class Submission {
    private final String id;

    private final String taskId;

    private final String userId;

    private final String code;
}
