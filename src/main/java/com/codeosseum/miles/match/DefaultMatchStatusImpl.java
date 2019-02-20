package com.codeosseum.miles.match;

import java.util.concurrent.atomic.AtomicReference;

public class DefaultMatchStatusImpl implements MatchStatus {
    private final AtomicReference<String> currentStage;

    private final AtomicReference<String> currentMode;

    private String id;

    public DefaultMatchStatusImpl() {
        this.currentStage = new AtomicReference<>(UNSET_STAGE);
        this.currentMode = new AtomicReference<>(UNSET_MODE);
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
}
