package com.codeosseum.miles.chat.controller.incoming;

import lombok.Value;

@Value
public final class MessagesSinceRequest {
    private final long timestamp;

    private final String roomId;
}
