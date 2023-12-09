package org.example.monitor;

import org.example.model.Player;
import org.example.network.TelegramBot;
import org.example.service.GameFactory;
import org.example.service.MessageHandler;
import org.example.service.PlayerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public interface PlayerMonitor extends MessageHandler {
    ConcurrentLinkedQueue<Player> throwInFoolWaiters = new ConcurrentLinkedQueue<>();

    default void addPlayerToPlayerMonitor(TelegramBot bot, Long chatId) {
        //fixme DEBUG
        System.out.println("DEBUG: PlayerMonitor.addPlayerToPlayerMonitor starts for chatId: " + chatId);
        new Thread(() -> {
            Player player = new PlayerFactory(bot, chatId).createPlayer();
            System.out.println("DEBUG: PlayerMonitor.addThrowInFoolWaiter added player: " + player);
            throwInFoolWaiters.add(player);
            //fixme DEBUG
            System.out.println("DEBUG: PlayerMonitor.addThrowInFoolWaiter contains: " + throwInFoolWaiters.size() + " players");
            if (throwInFoolWaiters.size() == 2) {
                new GameFactory(bot).createGame();
            }
            else sendMessageTo(chatId, "Ждем игроков...");
        }).start();
    }

    default List<Player> getThrowInFoolWaiterList() {
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            players.add(throwInFoolWaiters.poll());
        }
        //fixme DEBUG
        System.out.println("DEBUG: PlayerMonitor.getThrowInFoolWaiterList returns: " + players);
        return players;
    }

    default int getSize() {
        return throwInFoolWaiters.size();
    }
}
