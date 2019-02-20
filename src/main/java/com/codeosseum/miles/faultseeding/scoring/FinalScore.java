package com.codeosseum.miles.faultseeding.scoring;

import java.util.List;

import lombok.Value;

@Value
public class FinalScore {
    private final List<String> winners;

    private final List<Position> ranking;

    private final Result result;

    public enum Result {
        WIN, DRAW
    }
}
