package org.example.BusinessLayer.throwInFool;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.example.EntityLayer.Player;
import org.example.ServiseLayer.factories.GameFactory;
import org.example.ServiseLayer.monitors.PlayersMonitor;
import org.example.ServiseLayer.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This class implements PlayersMonitor, and it is responsible for managing players in ThrowInFool queue and running a ThrowInFool game if the number of players is equal to or greater than the maximum number of players.
 */
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ThrowinfoolPlayerMonitor implements PlayersMonitor {
    GameFactory gameFactory;
    MessageService messageService;
    ConcurrentLinkedQueue<Player> throwInFoolWaiters = new ConcurrentLinkedQueue<>();

    /**
     * The maximum number of players allowed in the game and defined in the application.properties file.
     */
    @Value("${game.maxPlayers}")
    @NonFinal
    int MAX_PLAYERS;

    /**
     * Creates a new ThrowinfoolMonitor instance.
     *
     * @param gameFactory The abstract game factory to create games.
     * @param messageService      The message service to send messages.
     */
    @Autowired
    public ThrowinfoolPlayerMonitor(GameFactory gameFactory, MessageService messageService) {
        this.gameFactory = gameFactory;
        this.messageService = messageService;
    }

    /**
     * Adds a player to the game and runs the game if the number of players is equal to or greater than the maximum number of players.
     *
     * @param player   The player to add.
     * @param gameType The specified game type to run the concrete game.
     */

    @Override
    public void addPlayer(String gameType, Player player) {
        throwInFoolWaiters.add(player);
        runGameIfPlayersEquals(MAX_PLAYERS, player, gameType);
    }

    /**
     * Runs the game if the number of players is equal to or greater than the maximum number of players.
     *
     * @param MAX_PLAYERS The maximum number of players allowed in the game.
     * @param player      The player to add.
     * @param gameType    The command to run the game.
     */
    @Override
    public void runGameIfPlayersEquals(int MAX_PLAYERS, Player player, String gameType) {
        if (getQueueSize() >= MAX_PLAYERS) {
            gameFactory.create(gameType, getPlayers());
        } else {
            messageService.sendMessageTo(player.getChatID(), "Ждем игроков");
        }
    }

    /**
     * Gets a list of players from the queue.
     *
     * @return The list of players.
     */
    @Override
    public List<Player> getPlayers() {
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < MAX_PLAYERS; i++) {
            players.add(throwInFoolWaiters.poll());
        }
        return players;
    }

    /**
     * Gets the size of the queue.
     *
     * @return The size of the queue.
     */
    @Override
    public int getQueueSize() {
        return throwInFoolWaiters.size();
    }
}