package com.codeosseum.miles.chat.room;

import java.util.List;
import java.util.Optional;

public interface RoomService {
    void createRoom(String id, String displayName, List<String> participants);

    List<Room> getAllRooms();

    Optional<Room> getRoomById(String id);

    List<Room> getRoomsIncluding(String participant);

    void deleteAllRooms();
}
