package com.codeosseum.miles.faultseeding.scoring;

import java.util.List;

import lombok.Value;

@Value
public final class Position {
    private final List<String> players;

    private final int score;
}
