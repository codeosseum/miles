package com.codeosseum.miles.chat;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class ChatMessage {
    private final String sender;

    private final LocalDateTime timestamp;

    private final String roomId;

    private final String contents;

    private final String type;
}
