package com.codeosseum.miles.player.event;

import lombok.Value;

@Value
public class PlayerJoinedEvent {
    private final String id;
}
