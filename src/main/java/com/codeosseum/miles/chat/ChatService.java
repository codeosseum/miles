package com.codeosseum.miles.chat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ChatService {
    void createRoom(String id, String displayName, List<String> participants);

    List<Room> getAllRooms();

    Optional<Room> getRoomById(String id);

    List<Room> getRoomsIncluding(String participant);

    void deleteAllRooms();

    void sendMessage(ChatMessage message);

    List<ChatMessage> getAllMessagesForRoom(String roomId);

    List<ChatMessage> getMessageForRoomSince(String roomId, LocalDateTime since);
}
