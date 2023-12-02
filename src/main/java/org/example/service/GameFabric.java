package org.example.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.game.ThrowInFool;
import org.example.monitor.GameMonitor;
import org.example.monitor.PlayerMonitor;
import org.example.network.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
@AllArgsConstructor(onConstructor_ = {@Autowired})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GameFabric {

    public void runThrowInFoolAsync() {
        System.out.println("GameMonitor.runThrowInFoolAsync starts");
        CompletableFuture<Void> future = new CompletableFuture<>();
        UUID gameId = UUID.randomUUID();
        CompletableFuture.runAsync(() -> {
            new ThrowInFool(gameId).play();
            // Помечаем CompletableFuture как завершенный
            future.complete(null);
        });
    }
}
