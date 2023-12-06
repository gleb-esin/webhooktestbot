package org.example.monitor;

import org.example.model.Player;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public interface GameMonitor {
     ConcurrentHashMap<UUID, List<Player>> throwInFoolGames = new ConcurrentHashMap<>();

    default void addThrowInFoolToGameMonitor(UUID gameId, List<Player> players) {
        throwInFoolGames.put(gameId, players);
    }
}

