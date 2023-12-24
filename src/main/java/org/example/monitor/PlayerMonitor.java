package org.example.monitor;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.model.Player;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlayerMonitor {
    ConcurrentLinkedQueue<Player> throwInFoolWaiters = new ConcurrentLinkedQueue<>();

    public void addPlayerToThrowInFoolWaiters(Player player) {
        throwInFoolWaiters.add(player);
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
