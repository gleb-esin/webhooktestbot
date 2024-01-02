package org.example.monitor;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.model.Player;
import org.example.service.GameFactory;
import org.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlayerMonitor {
    GameFactory gameFactory;
    MessageService messageService;
    ConcurrentLinkedQueue<Player> throwInFoolWaiters = new ConcurrentLinkedQueue<>();

    public void addPlayerToThrowInFoolWaiters(Player player) {
        throwInFoolWaiters.add(player);
        if (getThrowInFoolWaiterListSize() == 2) {
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

    public int getThrowInFoolWaiterListSize() {
        return throwInFoolWaiters.size();
    }
}
