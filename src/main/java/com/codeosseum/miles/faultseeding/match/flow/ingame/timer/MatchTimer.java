package com.codeosseum.miles.faultseeding.match.flow.ingame.timer;

import java.time.LocalDateTime;

public interface MatchTimer {
    void start();

    LocalDateTime startedAt();
}
