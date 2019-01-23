package com.codeosseum.miles.chat.controller.outgoing;

import com.codeosseum.miles.chat.Room;
import lombok.Value;

import java.util.List;

@Value
public final class RoomsOfUser {
    private final List<Room> rooms;
}
