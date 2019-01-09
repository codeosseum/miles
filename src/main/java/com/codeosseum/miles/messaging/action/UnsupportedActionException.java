package com.codeosseum.miles.messaging.action;

public class UnsupportedActionException extends Exception {
    private final Action action;

    public UnsupportedActionException(Action action) {
        super("Unsupported action: " + action);

        this.action = action;
    }

    public Action getAction() {
        return action;
    }
}

