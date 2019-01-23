package com.codeosseum.miles.chat;

import lombok.Value;

import java.util.List;

@Value
public class Room {
    private final String id;

    private final String displayName;

    private final List<String> participants;
}
