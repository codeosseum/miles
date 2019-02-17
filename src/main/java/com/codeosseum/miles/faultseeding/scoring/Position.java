package com.codeosseum.miles.faultseeding.scoring;

import lombok.Value;

@Value
public final class Position {
    private final String playerId;

    private final int score;
}
