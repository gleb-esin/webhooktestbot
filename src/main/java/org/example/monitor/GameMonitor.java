package org.example.monitor;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GameMonitor {
    private ConcurrentHashMap<UUID, List<Player>> throwInFoolGames = new ConcurrentHashMap<>();

    public void addThrowInFoolGame(UUID gameId, List<Player> players) {
        throwInFoolGames.put(gameId, players);
    }
}

