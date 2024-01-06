package org.example.interfaceAdapters.monitor;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.example.entities.Player;
import org.example.interfaceAdapters.factories.AbstractGameFactory;
import org.example.interfaceAdapters.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This class represents a ThrowinfoolMonitor, which is a subclass of Monitor.
 * It is responsible for managing players and running a game if the number of players is equal to or greater than the maximum number of players.
 */
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ThrowinfoolMonitor extends Monitor {
    AbstractGameFactory abstractGameFactory;
    MessageService messageService;
    ConcurrentLinkedQueue<Player> throwInFoolWaiters = new ConcurrentLinkedQueue<>();

    /** The maximum number of players allowed in the game. */
    @Value("${game.maxPlayers}")
    @NonFinal
    int MAX_PLAYERS;

    /**
     * Creates a new ThrowinfoolMonitor instance.
     * @param abstractGameFactory The abstract game factory to create games.
     * @param messageService The message service to send messages.
     */
    @Autowired
    public ThrowinfoolMonitor(AbstractGameFactory abstractGameFactory, MessageService messageService) {
        this.abstractGameFactory = abstractGameFactory;
        this.messageService = messageService;
    }

    /**
     * Adds a player to the game and runs the game if the number of players is equal to or greater than the maximum number of players.
     * @param player The player to add.
     * @param command The command to run the game.
     */
    @Override
    public void addPlayer(Player player, String command) {
        throwInFoolWaiters.add(player);
        runGameIfPlayersEquals(MAX_PLAYERS, player, command);
    }

    /**
     * Runs the game if the number of players is equal to or greater than the maximum number of players.
     * @param MAX_PLAYERS The maximum number of players allowed in the game.
     * @param player The player to add.
     * @param command The command to run the game.
     */
    @Override
    protected void runGameIfPlayersEquals(int MAX_PLAYERS, Player player, String command) {
        if (getQueueSize() >= MAX_PLAYERS) {
            abstractGameFactory.create(command, getPlayers());
        } else {
            messageService.sendMessageTo(player.getChatID(), "Ждем игроков");
        }
    }

    /**
     * Gets a list of players from the queue.
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
     * @return The size of the queue.
     */
    @Override
    public int getQueueSize() {
        return throwInFoolWaiters.size();
    }
}