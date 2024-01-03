package org.example.monitor;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.example.model.Player;
import org.example.service.GameFactory;
import org.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlayerMonitor {
    GameFactory gameFactory;
    MessageService messageService;
    ConcurrentLinkedQueue<Player> throwInFoolWaiters = new ConcurrentLinkedQueue<>();
    @Value("${game.maxPlayers}")
    @NonFinal
    int MAX_PLAYERS;

    @Autowired
    public PlayerMonitor(GameFactory gameFactory, MessageService messageService) {
        this.gameFactory = gameFactory;
        this.messageService = messageService;
    }

    public void addPlayerToThrowInFoolWaiters(Player player) {
        throwInFoolWaiters.add(player);
        runThrowInFoolGameIfPlayersEquals(MAX_PLAYERS, player);
    }

    private void runThrowInFoolGameIfPlayersEquals(int MAX_PLAYERS, Player player) {
        if (getThrowInFoolWaiterSize() == MAX_PLAYERS) {
            gameFactory.createThrowInFoolGame(getThrowInFoolWaiterList());
        } else {
            messageService.sendMessageTo(player.getChatID(), "Ждем игроков");
        }
    }

    public List<Player> getThrowInFoolWaiterList() {
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            players.add(throwInFoolWaiters.poll());
        }
        return players;
    }

    public int getThrowInFoolWaiterSize() {
        return throwInFoolWaiters.size();
    }
}
