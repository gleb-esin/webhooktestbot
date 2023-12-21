package org.example.monitor;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.model.Player;
import org.example.network.TelegramBot;
import org.example.service.MessageService;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GameMonitor implements MessageService {
    ConcurrentHashMap<UUID, List<Player>> throwInFoolGames = new ConcurrentHashMap<>();

    public void addThrowInFoolToGameMonitor(UUID gameId, List<Player> players) {
        throwInFoolGames.put(gameId, players);
    }

    public void removeThrowInFoolToGameMonitor(UUID gameId, TelegramBot bot) {
        List<Player> players = throwInFoolGames.remove(gameId);
        sendMessageToAll(players, "Игра  завершена.\n Выберите что-нибудь из меню", bot);
    }
}

