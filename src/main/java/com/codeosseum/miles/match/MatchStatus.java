package com.codeosseum.miles.match;

public interface MatchStatus {
    String NO_MATCH = null;

    String getCurrentStage();

    void setCurrentStage(String stage);

    String getCurrentMode();

    void setCurrentMode(String mode);
}
