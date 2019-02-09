package com.codeosseum.miles.player;

import com.google.inject.Inject;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.util.Objects.requireNonNull;

public class DefaultRegisteredPlayerRegistryImpl implements RegisteredPlayerRegistry {
    private final List<String> players;

    @Inject
    public DefaultRegisteredPlayerRegistryImpl() {
        players = new CopyOnWriteArrayList<>();
    }

    @Override
    public boolean hasPlayer(final String id) {
        return players.contains(requireNonNull(id));
    }

    @Override
    public List<String> getAllPlayers() {
        return Collections.unmodifiableList(players);
    }

    @Override
    public void addPlayer(final String id) {
        players.add(requireNonNull(id));
    }

    @Override
    public void deletePlayer(final String id) {
        players.remove(requireNonNull(id));
    }

    @Override
    public void deleteAllPlayers() {
        players.clear();
    }
}
