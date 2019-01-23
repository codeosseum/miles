package com.codeosseum.miles.chat.controller.incoming;

import lombok.Value;

@Value
public class IncomingChatMessage {
    private final long timestamp;

    private final String roomId;

    private final String contents;

    private final String type;
}
