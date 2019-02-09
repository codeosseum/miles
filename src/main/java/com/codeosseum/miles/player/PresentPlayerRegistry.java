package com.codeosseum.miles.player;

import java.util.List;

public interface PresentPlayerRegistry {
    boolean hasPlayer(String id);

    List<String> getAllPlayers();

    void addPlayer(String id);

    void deletePlayer(String id);

    void deleteAllPlayers();
}
