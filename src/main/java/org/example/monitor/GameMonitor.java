package org.example.monitor;

import org.example.model.Player;
import org.example.service.MessageService;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public interface GameMonitor extends MessageService {
     ConcurrentHashMap<UUID, List<Player>> throwInFoolGames = new ConcurrentHashMap<>();

    default void addThrowInFoolToGameMonitor(UUID gameId, List<Player> players) {
        throwInFoolGames.put(gameId, players);
    }

    default void removeThrowInFoolToGameMonitor(UUID gameId) {
        List<Player> players = throwInFoolGames.remove(gameId);
        sendMessageToAll(players, "Игра  завершена.\n Выберите что-нибудь из меню");
    }
}

