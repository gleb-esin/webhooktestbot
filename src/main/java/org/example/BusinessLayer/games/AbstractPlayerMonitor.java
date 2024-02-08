package org.example.BusinessLayer.games;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.EntityLayer.Player;
import org.example.ServiseLayer.factories.GameFactory;
import org.example.ServiseLayer.services.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
public abstract class AbstractPlayerMonitor {
    GameFactory gameFactory;
    MessageService messageService;
    ConcurrentLinkedQueue<Player> waiters = new ConcurrentLinkedQueue<>();
    int MAX_PLAYERS = 2;

    /**
     * Adds a player to the game and runs the game if the number of players is equal to or greater than the maximum number of players.
     *
     * @param player   The player to add.
     * @param gameType The specified game type to run the concrete game.
     */

    public void addPlayer(String gameType, Player player) {
        waiters.add(player);
        runGameIfPlayersEquals(MAX_PLAYERS, player, gameType);
    }

    /**
     * Runs the game if the number of players is equal to or greater than the maximum number of players.
     *
     * @param MAX_PLAYERS The maximum number of players allowed in the game.
     * @param player      The player to add.
     * @param gameType    The command to run the game.
     */
    public void runGameIfPlayersEquals(int MAX_PLAYERS, Player player, String gameType) {
        if (getQueueSize() >= MAX_PLAYERS) {
            gameFactory.runGame(gameType, getPlayers());
        } else {
            messageService.sendMessageTo(player.getChatID(), "Ждем игроков");
        }
    }

    /**
     * Gets a list of players from the queue.
     *
     * @return The list of players.
     */
    public List<Player> getPlayers() {
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < MAX_PLAYERS; i++) {
            players.add(waiters.poll());
        }
        return players;
    }

    /**
     * Gets the size of the queue.
     *
     * @return The size of the queue.
     */
    public int getQueueSize() {
        return waiters.size();
    }
}
