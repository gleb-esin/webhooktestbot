package org.example.ServiseLayer.factories;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.example.BusinessLayer.games.Game;
import org.example.DataLayer.UserEntityRepository;
import org.example.EntityLayer.Player;
import org.example.EntityLayer.UserEntity;
import org.example.ServiseLayer.monitors.GameMonitor;
import org.example.ServiseLayer.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Semaphore;

/**
 * The GameFactory class is responsible for creating a game based on the provided game type and list of players.
 */
@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GameFactory {
    GameMonitor gameMonitor;
    UserEntityRepository userEntityRepository;
    MessageService messageService;
    Map<String, Game> games;

    Semaphore semaphore;
    @NonFinal
    volatile boolean isServersAreBusyMessageSent = false;

    @Autowired
    public GameFactory(GameMonitor gameMonitor, UserEntityRepository userEntityRepository, MessageService messageService, Map<String, Game> games, Semaphore semaphore) {
        this.gameMonitor = gameMonitor;
        this.userEntityRepository = userEntityRepository;
        this.messageService = messageService;
        this.games = games;
        this.semaphore = semaphore;
    }

    /**
     * Creates a new game of the specified type with the given players.
     *
     * @param gameType the type of game to create
     */
    public Game getGame(String gameType) {
        if (games.containsKey(gameType)) {
            return games.get(gameType);
        } else {
            log.error("GameFactory.create(): Unsupported game type: " + gameType);
            return null;
        }
    }

    public void runGame(String gameType, List<Player> players) {
        if (semaphore.availablePermits() == 0) {
            isServersAreBusyMessageSent = true;
            messageService.sendMessageToAll(players, "Все игровые сервера заняты, подождите, пожалуйста");
        }
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (isServersAreBusyMessageSent) {
            messageService.sendMessageToAll(players, "Добро пожаловать в игру!");
            isServersAreBusyMessageSent = false;
        }
        UUID gameID = UUID.randomUUID();
        gameMonitor.addSession(gameID, players);
        getGame(gameType).play(players);
        finnishGame(players, UUID.randomUUID());
        semaphore.release();
    }

    public void finnishGame(List<Player> players, UUID gameID) {
        gameMonitor.removeGame(players);
        for (Player player : players) {
            UserEntity userEntity = new UserEntity(player);
            userEntityRepository.save(userEntity);
        }

    }
}

