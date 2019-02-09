package com.codeosseum.miles.faultseeding.match.registration;

import com.codeosseum.miles.faultseeding.match.configuration.MatchConfiguration;
import lombok.Value;

@Value
public class FaultSeedingMatchRegisteredEvent {
    private final MatchConfiguration matchConfiguration;
}
