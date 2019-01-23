package com.codeosseum.miles.chat.room;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static java.util.Collections.unmodifiableList;
import static java.util.Objects.requireNonNull;

public class DefaultRoomServiceImpl implements RoomService {
    private final List<Room> rooms;

    private DefaultRoomServiceImpl() {
        this.rooms = new CopyOnWriteArrayList<>();
    }

    @Override
    public void createRoom(final String id, final String displayName, final List<String> participants) {
        final Room room = new Room(requireNonNull(id), requireNonNull(displayName), requireNonNull(participants));

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
        rooms.clear();
    }
}
