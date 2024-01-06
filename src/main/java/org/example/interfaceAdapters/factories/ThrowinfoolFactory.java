package org.example.interfaceAdapters.factories;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.example.entities.GameType;
import org.example.entities.Player;
import org.example.interfaceAdapters.monitor.GameMonitor;
import org.example.frameworks.UserEntityRepository;
import org.example.interfaceAdapters.service.MessageService;
import org.example.usecases.states.ThrowInFool;
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
public class ThrowinfoolFactory implements GameFactory {
    GameMonitor gameMonitor;
    UserEntityRepository userEntityRepository;
    MessageService messageService;
    @Value("${game.maxAllowedParallelGames}")
    @NonFinal
    int MAX_ALLOWED_PARALLEL_GAMES;
    GameType gameType = GameType.THROW_IN_FOOL;

    @Autowired
    public ThrowinfoolFactory(GameMonitor gameMonitor, UserEntityRepository userEntityRepository, MessageService messageService) {
        this.gameMonitor = gameMonitor;
        this.userEntityRepository = userEntityRepository;
        this.messageService = messageService;
    }

    @Override
    public void create(List<Player> players) {
        UUID gameID = UUID.randomUUID();
        gameMonitor.addThrowInFoolToGameMonitor(gameID, players);
        ExecutorService executorService = Executors.newCachedThreadPool(); // Мы используем CachedThreadPool
        Semaphore semaphore = new Semaphore(MAX_ALLOWED_PARALLEL_GAMES); // Начальное количество разрешений
        executorService.submit(() -> {
            try {
                // Ожидаем, пока не получим разрешение от семафора
                semaphore.acquire();
                // Код для каждого отдельного игрока
                new ThrowInFool(messageService, gameID, players).play();
                finnish(players, gameID);
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
    public void finnish(List<Player> players, UUID gameID) {
        gameMonitor.removeThrowInFoolToGameMonitor(gameID);
        for (Player player : players) {
            userEntityRepository.save(player.toUserEntity());
        }
    }
}
