package org.example.monitor;

import org.example.model.Player;
import org.example.service.GameFactory;
import org.example.service.MessageService;
import org.example.service.PlayerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public interface PlayerMonitor extends MessageService {
    ConcurrentLinkedQueue<Player> throwInFoolWaiters = new ConcurrentLinkedQueue<>();

    default void addPlayerToPlayerMonitor(Long chatId) {
        new Thread(() -> {
            Player player = new PlayerFactory(chatId).createPlayer();
            throwInFoolWaiters.add(player);
            if (throwInFoolWaiters.size() == 2) {
                new GameFactory().createThrowInFoolGame();
            }
            else sendMessageTo(chatId, "Ждем игроков...");
        }).start();
    }

    default List<Player> getThrowInFoolWaiterList() {
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            players.add(throwInFoolWaiters.poll());
        }
        return players;
    }

    default int getSize() {
        return throwInFoolWaiters.size();
    }
}
