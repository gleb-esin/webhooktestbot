package org.example.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.game.Game;
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
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GameFabric {

    public void runThrowInFoolAsync(TelegramBot bot) {
        CompletableFuture.runAsync(() -> {
            //fixme DEBUG
            System.out.println("DEBUG: GameFabric.runThrowInFoolAsync runAsync");
            ThrowInFool throwInFool = new ThrowInFool(UUID.randomUUID());
            throwInFool.setBot(bot);
            throwInFool.setGameMonitor(bot.getGameMonitor());
            throwInFool.setPlayerMonitor(bot.getPlayerMonitor());
            throwInFool.play();
        });
    }
}
