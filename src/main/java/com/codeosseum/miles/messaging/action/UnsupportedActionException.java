package com.codeosseum.miles.messaging.action;

public class UnsupportedActionException extends Exception {
    private final String action;

    public UnsupportedActionException(String action) {
        super("Unsupported action: " + action);

        this.action = action;
    }

    public String getAction() {
        return action;
    }
}

