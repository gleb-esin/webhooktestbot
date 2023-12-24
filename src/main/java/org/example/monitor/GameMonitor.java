package org.example.monitor;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.model.Player;
import org.example.network.TelegramBot;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GameMonitor {
    ConcurrentHashMap<UUID, List<Player>> throwInFoolGames = new ConcurrentHashMap<>();

    public void addThrowInFoolToGameMonitor(UUID gameId, List<Player> players) {
        throwInFoolGames.put(gameId, players);
    }

    public List<Player> getPlayers(UUID gameId) {
        return throwInFoolGames.get(gameId);
    }

    public void removeThrowInFoolToGameMonitor(UUID gameId, TelegramBot bot) {
        List<Player> players = throwInFoolGames.remove(gameId);
        bot.sendMessageToAll(players, "Игра  завершена.\n Выберите что-нибудь из меню");
    }
}

