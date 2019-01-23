package com.codeosseum.miles.chat;

public interface ChatService {
    void sendMessage(ChatMessage message, String receivingRoomId);
}
