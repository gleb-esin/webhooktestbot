package org.example.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.example.model.Player;
import org.example.monitor.GameMonitor;
import org.example.network.UserEntityRepository;
import org.example.state.ThrowInFool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GameFactory {
    GameMonitor gameMonitor;
    UserEntityRepository userEntityRepository;
    MessageService messageService;
    @Value("${game.maxAllowedParallelGames}")
    @NonFinal
    int MAX_ALLOWED_PARALLEL_GAMES;

    @Autowired
    public GameFactory(GameMonitor gameMonitor, UserEntityRepository userEntityRepository, MessageService messageService) {
        this.gameMonitor = gameMonitor;
        this.userEntityRepository = userEntityRepository;
        this.messageService = messageService;
    }

    public void createThrowInFoolGame(List<Player> players) {
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
                finnishGame(players, gameID);
            } catch (InterruptedException e) {
                // Обработка исключения
            } finally {
                // Освобождаем разрешение после завершения игры
                semaphore.release();
            }
        });
        executorService.shutdown();
    }

    public void finnishGame(List<Player> players, UUID gameID) {
        gameMonitor.removeThrowInFoolToGameMonitor(gameID);
        for (Player player : players) {
            userEntityRepository.save(player.toUserEntity());
        }
    }
}
