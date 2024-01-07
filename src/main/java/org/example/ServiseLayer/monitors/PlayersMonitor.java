package org.example.ServiseLayer.monitors;

import org.example.EntityLayer.Player;
import java.util.List;

public interface PlayersMonitor {

    void addPlayer(String gameType, Player player);

    void runGameIfPlayersEquals(int MAX_PLAYERS, Player player, String gameType);

    List<Player> getPlayers();

    int getQueueSize();
}
