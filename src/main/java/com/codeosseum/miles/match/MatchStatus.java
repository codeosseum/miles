package com.codeosseum.miles.match;

public interface MatchStatus {
    String UNSET_MODE = "";

    String UNSET_STAGE = "";

    String UNSET_ID = "";

    String UNSET_JOIN_PASSOWRD = "";

    String getCurrentStage();

    void setCurrentStage(String stage);

    String getCurrentMode();

    void setCurrentMode(String mode);

    String getId();

    void setId(String id);

    String getJoinPassword();

    void setJoinPassword(String joinPassword);
}
