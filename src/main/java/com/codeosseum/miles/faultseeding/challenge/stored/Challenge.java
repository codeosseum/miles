package com.codeosseum.miles.faultseeding.challenge.stored;

import lombok.Value;

import java.util.List;

@Value
public class Challenge {
    private final String id;

    private final int difficulty;

    private final String entrypointPath;

    private final String descriptionPath;

    private final List<Solution> solutions;
}
