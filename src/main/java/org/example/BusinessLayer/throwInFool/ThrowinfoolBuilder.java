package org.example.BusinessLayer.throwInFool;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.example.EntityLayer.Player;
import org.example.DataLayer.UserEntityRepository;
import org.example.ServiseLayer.factories.GameBuilder;
import org.example.ServiseLayer.monitors.GameMonitor;
import org.example.ServiseLayer.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ThrowinfoolBuilder implements GameBuilder {
    GameMonitor gameMonitor;
    UserEntityRepository userEntityRepository;
    MessageService messageService;
    @Value("${game.maxAllowedParallelGames}")
    @NonFinal
    int MAX_ALLOWED_PARALLEL_GAMES;

    @Autowired
    public ThrowinfoolBuilder(GameMonitor gameMonitor, UserEntityRepository userEntityRepository, MessageService messageService) {
        this.gameMonitor = gameMonitor;
        this.userEntityRepository = userEntityRepository;
        this.messageService = messageService;
    }

    @Override
    public void buildGame(List<Player> players) {
        UUID gameID = UUID.randomUUID();
        gameMonitor.addGame(gameID, players);
        ExecutorService executorService = Executors.newCachedThreadPool(); // Мы используем CachedThreadPool
        Semaphore semaphore = new Semaphore(MAX_ALLOWED_PARALLEL_GAMES); // Начальное количество разрешений
        executorService.submit(() -> {
            try {
                // Ожидаем, пока не получим разрешение от семафора
                semaphore.acquire();
                // Код для каждого отдельного игрока
                new ThrowInFool(messageService, gameID, players).play();
                finnishGame(players, gameID);
            } catch (InterruptedException e) {
                log.error("ThrowinfoolFactory.create(): " + e.getMessage());
            } finally {
                // Освобождаем разрешение после завершения игры
                semaphore.release();
            }
        });
        executorService.shutdown();
    }

    @Override
    public void finnishGame(List<Player> players, UUID gameID) {
        gameMonitor.removeGame(gameID);
        for (Player player : players) {
            userEntityRepository.save(player.toUserEntity());
        }
    }
}
