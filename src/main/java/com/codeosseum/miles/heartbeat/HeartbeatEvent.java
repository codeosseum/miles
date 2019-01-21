package com.codeosseum.miles.heartbeat;

import lombok.Value;

@Value
public class HeartbeatEvent {
    public static final String IDENTIFIER = "heartbeat";

    private final String serverIdentifier;
}
