package com.codeosseum.miles.match;

import java.util.concurrent.atomic.AtomicReference;

public class DefaultMatchStatusImpl implements MatchStatus {
    private AtomicReference<String> currentStage;

    private AtomicReference<String> currentMode;

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
}
