package com.codeosseum.miles.chat;

import java.util.List;

public interface RoomService {
    void createRoom(String id, String displayName, List<String> participants);

    List<Room> getAllRooms();

    List<Room> getRoomById(String id);

    List<Room> getRoomsIncluding(String participant);

    void deleteAllRooms();
}
