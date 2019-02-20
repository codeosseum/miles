package com.codeosseum.miles.faultseeding.match.flow.cleanup;

import java.time.LocalDateTime;

public interface MatchTimer {
    void start();

    LocalDateTime startedAt();
}
