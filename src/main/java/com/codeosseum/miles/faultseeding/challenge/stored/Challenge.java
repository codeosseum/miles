package com.codeosseum.miles.faultseeding.challenge.stored;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class Challenge {
    private final String id;

    private final String title;

    private final int difficulty;

    private final String evaluatorEntrypointPath;

    private final String descriptionPath;

    private final List<Solution> solutions;
}
