package org.example.monitor;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.model.Player;
import org.example.network.TelegramBot;
import org.example.service.GameFactory;
import org.example.service.MessageService;
import org.example.service.PlayerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlayerMonitor implements MessageService {
    ConcurrentLinkedQueue<Player> throwInFoolWaiters = new ConcurrentLinkedQueue<>();

    public void addPlayerToThrowInFoolWaiters(Long chatId, TelegramBot bot) {
        new Thread(() -> {
            Player player = new PlayerFactory(chatId, bot).createPlayer();
            throwInFoolWaiters.add(player);
            if (throwInFoolWaiters.size() == 2) {
                new GameFactory(bot).createThrowInFoolGame();
            }
            else sendMessageTo(chatId, "Ждем игроков...", bot);
        }).start();
    }

    public List<Player> getThrowInFoolWaiterList() {
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            players.add(throwInFoolWaiters.poll());
        }
        return players;
    }
}
