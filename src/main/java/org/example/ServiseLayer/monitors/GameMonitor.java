package org.example.ServiseLayer.monitors;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.EntityLayer.Player;
import org.example.ServiseLayer.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
    ConcurrentHashMap<Player, UUID> games = new ConcurrentHashMap<>();

    /**
     * Adds a game to the map of games.
     *
     * @param gameId  the ID of the game to be added
     * @param players the list of players in the game
     */
    public void addSession(UUID gameId, List<Player> players) {
        for (Player player : players) {
            games.put(player, gameId);
        }
    }

    /**
     * Retrieves the list of players for a given game ID.
     *
     * @param gameId the UUID of the game
     * @return the list of players for the specified game ID
     */
    public List<Player> getPlayersList(UUID gameId) {
        List<Player> players = new ArrayList<>();
        games.forEach((player, uuid) -> {
            if (uuid.equals(gameId)) {
                players.add(player);
            }
        });
        return players;
    }

    public UUID getGameId(Player player) {
        UUID gameId = games.get(player);
        return gameId;
    }

    /**
     * Removes a game from the list of games.
     */
    public void removeGame(List<Player> players) {
        for (Player player : players) {
            games.remove(player);
            messageService.sendMessageTo(player, "Игра  завершена.\n Выберите что-нибудь из меню");
        }
    }

    public void removeGame(UUID uuid) {
        games.forEach((player, uuid1) -> {
            if (uuid1.equals(uuid)) {
                games.remove(player);
            }
        });
    }
}

