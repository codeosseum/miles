package com.codeosseum.miles.faultseeding.match.over;

import java.time.LocalDateTime;

public interface MatchTimer {
    void start();

    LocalDateTime startedAt();
}
