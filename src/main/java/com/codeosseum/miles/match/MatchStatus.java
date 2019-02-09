package com.codeosseum.miles.match;

public interface MatchStatus {
    String UNSET_MODE = "";

    String UNSET_STAGE = "";

    String getCurrentStage();

    void setCurrentStage(String stage);

    String getCurrentMode();

    void setCurrentMode(String mode);
}
