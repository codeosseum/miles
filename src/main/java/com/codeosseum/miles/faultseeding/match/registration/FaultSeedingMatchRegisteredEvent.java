package com.codeosseum.miles.faultseeding.match.registration;

import com.codeosseum.miles.faultseeding.match.configuration.MatchConfiguration;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class FaultSeedingMatchRegisteredEvent {
    private final String id;

    private final MatchConfiguration matchConfiguration;

    private final String joinPassword;
}
