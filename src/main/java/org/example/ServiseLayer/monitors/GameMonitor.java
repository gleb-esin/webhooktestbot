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

    /**
     * Adds a game to the map of games.
     *
     * @param  gameId   the ID of the game to be added
     * @param  players  the list of players in the game
     */
    public void addGame(UUID gameId, List<Player> players) {
        games.put(gameId, players);
    }

    /**
     * Retrieves the list of players for a given game ID.
     *
     * @param  gameId  the UUID of the game
     * @return         the list of players for the specified game ID
     */
    public List<Player> getPlayers(UUID gameId) {
        return games.get(gameId);
    }

    /**
     * Removes a game from the list of games.
     *
     * @param  gameId  the ID of the game to be removed
     */
    public void removeGame(UUID gameId) {
        List<Player> players = games.remove(gameId);
        messageService.sendMessageToAll(players, "Игра  завершена.\n Выберите что-нибудь из меню");
    }
}

