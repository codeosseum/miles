package com.codeosseum.miles.faultseeding.challenge.stored;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Solution {
    public static final String ID_SEPARATOR = ":";

    public static final Integer MODE_INDEX = 0;
    public static final Integer CHALLENGE_NAME_INDEX = 1;
    public static final Integer HASH_INDEX = 2;

    private final String id;

    private final int difficulty;

    private final String entrypointPath;
}
