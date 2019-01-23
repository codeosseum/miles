package com.codeosseum.miles.chat.controller.outgoing;

import com.codeosseum.miles.chat.ChatMessage;
import lombok.Value;

import java.util.List;

@Value
public final class MessagesSince {
    private final List<ChatMessage> messages;
}
