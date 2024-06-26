package org.example.ServiseLayer.monitors;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.EntityLayer.Player;
import org.example.ServiseLayer.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class is responsible for monitoring games and players.
 */
@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GameMonitor {
    MessageService messageService;
    ConcurrentHashMap<UUID, List<Player>> games = new ConcurrentHashMap<>();

    public void addSession(UUID gameId, List<Player> players) {
        games.put(gameId, players);
    }


    public List<Player> getPlayers(UUID gameId) {
        return games.get(gameId);
    }

    public void removeGame(UUID gameId) {
        List<Player> players = games.remove(gameId);
        messageService.sendMessageToAll(players, "Игра  завершена.\n Выберите что-нибудь из меню");
    }
}

