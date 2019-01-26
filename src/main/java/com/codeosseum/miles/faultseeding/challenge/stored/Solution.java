package com.codeosseum.miles.faultseeding.challenge.stored;

import lombok.Value;

@Value
public class Solution {
    private final String id;

    private final int difficulty;

    private final String entrypointPath;
}
