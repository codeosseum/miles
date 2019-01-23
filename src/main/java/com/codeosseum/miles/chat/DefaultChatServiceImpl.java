package com.codeosseum.miles.chat;

import com.codeosseum.miles.communication.Message;
import com.codeosseum.miles.communication.push.PushMessageToClientService;
import com.google.inject.Inject;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static com.codeosseum.miles.communication.Message.message;
import static java.util.Collections.unmodifiableList;
import static java.util.Objects.requireNonNull;

public class DefaultChatServiceImpl implements ChatService {
    private static final String CHAT_MESSAGE = "chat-message";

    private final PushMessageToClientService pushMessageToClientService;

    private final List<Room> rooms;

    private final Map<String, LinkedList<ChatMessage>> messageMap;

    @Inject
    public DefaultChatServiceImpl(final PushMessageToClientService pushMessageToClientService) {
        this.pushMessageToClientService = pushMessageToClientService;
        this.rooms = new CopyOnWriteArrayList<>();
        this.messageMap = new ConcurrentHashMap<>();
    }

    @Override
    public void sendMessage(final ChatMessage message) {
        final Optional<Room> roomOptional = getRoomById(message.getRoomId());

        if (roomOptional.isPresent()) {
            final Room room = roomOptional.get();
            final Message<ChatMessage> outgoingMessage = message(CHAT_MESSAGE, message);

            messageMap.get(room.getId()).addFirst(message);

            room.getParticipants()
                    .forEach(participant -> pushMessageToClientService.sendMessage(participant, outgoingMessage));
        }
    }

    @Override
    public List<ChatMessage> getAllMessagesForRoom(final String roomId) {
        return Optional.ofNullable(messageMap.get(roomId)).orElseGet(LinkedList::new);
    }

    @Override
    public List<ChatMessage> getMessageForRoomSince(final String roomId, final LocalDateTime since) {
        final List<ChatMessage> messages = getAllMessagesForRoom(requireNonNull(roomId));
        final List<ChatMessage> result = new LinkedList<>();

        for (ChatMessage message : messages) {
            if (message.getTimestamp().isAfter(since)) {
                result.add(message);
            } else {
                break;
            }
        }

        return result;
    }

    @Override
    public void createRoom(final String id, final String displayName, final List<String> participants) {
        final Room room = new Room(requireNonNull(id), requireNonNull(displayName), requireNonNull(participants));

        messageMap.put(room.getId(), new LinkedList<>());

        rooms.add(room);
    }

    @Override
    public List<Room> getAllRooms() {
        return unmodifiableList(rooms);
    }

    @Override
    public Optional<Room> getRoomById(final String id) {
        return rooms.stream()
                .filter(room -> room.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Room> getRoomsIncluding(final String participant) {
        return rooms.stream()
                .filter(room -> room.getParticipants().contains(participant))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAllRooms() {
        messageMap.clear();
        rooms.clear();
    }
}
