package com.codeosseum.miles.faultseeding.task.publish;

import lombok.Value;

@Value
public class PublishedTask {
    private final String id;

    private final String title;

    private final String description;

    private final String code;
}
