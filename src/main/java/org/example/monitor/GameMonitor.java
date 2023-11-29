package org.example.monitor;

import org.example.model.Player;
import org.example.game.ThrowInFool;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class GameMonitor {
    private static ConcurrentHashMap<UUID, List<Player>> throwInFoolGames = new ConcurrentHashMap<>();

    public static void runThrowInFoolAsync() {
        System.out.println("GameMonitor.runThrowInFoolAsync starts");
        CompletableFuture<Void> future = new CompletableFuture<>();
        UUID gameId = UUID.randomUUID();
        CompletableFuture.runAsync(() -> {
                // Создаем и запускаем игру ThrowInFool
                ThrowInFool throwInFool = new ThrowInFool(gameId);
                throwInFool.play();
                // Помечаем CompletableFuture как завершенный
                future.complete(null);
        });
    }

    public static void addThrowInFoolPlayers(UUID gameId, List<Player> players) {
        throwInFoolGames.put(gameId, players);
    }

    public static List<Player> getThrowInFoolPlayers(UUID gameId) {
        return throwInFoolGames.get(gameId);
    }

    public static void deleteThrowInFoolPlayers(UUID gameId) {
        throwInFoolGames.remove(gameId);
    }
}

