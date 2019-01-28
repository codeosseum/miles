package com.codeosseum.miles.faultseeding.challenge.stored;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Solution {
    private final String id;

    private final int difficulty;

    private final String entrypointPath;
}
