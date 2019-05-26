package com.codeosseum.miles.configuration;

import lombok.Data;

@Data
public class FaultSeedingConfig {
    private int runtimeSeconds;

    private int startingCountdownSeconds;
}
