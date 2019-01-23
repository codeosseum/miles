package com.codeosseum.miles.chat;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageService {
    void addMessage(ChatMessage message, String receivingRoomId);

    List<ChatMessage> getAllMessagesForRoom(String roomId);

    List<ChatMessage> getMessageForRoomSince(LocalDateTime since);
}
