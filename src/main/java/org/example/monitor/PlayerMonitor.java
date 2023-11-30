package org.example.monitor;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Player;
import org.example.network.TelegramBot;
import org.example.service.GameFabric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlayerMonitor {
    GameFabric gameFabric;
    private ConcurrentLinkedQueue<Player> throwInFoolWaiters = new ConcurrentLinkedQueue<>();

    @Autowired
    public PlayerMonitor(GameFabric gameFabric) {
        this.gameFabric = gameFabric;
    }

    public BotApiMethod<?> addThrowInFoolWaiter(Player player) {
        System.out.println("PlayerMonitor.addThrowInFoolWaiter added player: " + player);
        throwInFoolWaiters.add(player);
        if (throwInFoolWaiters.size() > 1) {
            gameFabric.runThrowInFoolAsync();
            return null;
        } else {
            return new SendMessage(player.getChatID().toString(), "Ждем игроков...");
        }
    }

    public List<Player> getThrowInFoolWaiterList() {
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            players.add(throwInFoolWaiters.poll());
        }
        return players;
    }

    public boolean isPlayerAdded(Player player) {
        return throwInFoolWaiters.contains(player);
    }
}
