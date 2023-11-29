package org.example.monitor;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Player;
import org.example.game.ThrowInFool;
import org.example.network.TelegramBot;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.example.service.MessageHandler.sendNotificationTo;

@Component
@Slf4j
@Data
public class PlayerMonitor {
    private static ConcurrentLinkedQueue<Player> throwInFoolWaiters = new ConcurrentLinkedQueue<>();

    public static void addThrowInFoolWaiter(Player player) {
        System.out.println("PlayerMonitor.addThrowInFoolWaiter added player: " + player);
        throwInFoolWaiters.add(player);
        if (throwInFoolWaiters.size() > 1) {
            GameMonitor.runThrowInFoolAsync();
        } else {
            sendNotificationTo(player, "Ждем игроков...");

        }
    }

    public static List<Player> getThrowInFoolWaiterList() {
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            players.add(throwInFoolWaiters.poll());
        }
        return players;
    }
}
