package com.codeosseum.miles.match;

import java.util.concurrent.atomic.AtomicReference;

public class DefaultMatchStatusImpl implements MatchStatus {
    private final AtomicReference<String> currentStage;

    private final AtomicReference<String> currentMode;

    private final AtomicReference<String> joinPassword;

    private String id;

    public DefaultMatchStatusImpl() {
        this.currentStage = new AtomicReference<>(UNSET_STAGE);
        this.currentMode = new AtomicReference<>(UNSET_MODE);
        this.joinPassword = new AtomicReference<>(UNSET_JOIN_PASSOWRD);
        this.id = null;
    }

    @Override
    public String getCurrentStage() {
        return currentStage.get();
    }

    @Override
    public void setCurrentStage(final String currentStage) {
        this.currentStage.set(currentStage);
    }

    @Override
    public String getCurrentMode() {
        return currentMode.get();
    }

    @Override
    public void setCurrentMode(final String currentMode) {
        this.currentMode.set(currentMode);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(final String id) {
        this.id = id;
    }

    @Override
    public String getJoinPassword() {
        return joinPassword.get();
    }

    @Override
    public void setJoinPassword(final String joinPassword) {
        this.joinPassword.set(joinPassword);
    }
}
