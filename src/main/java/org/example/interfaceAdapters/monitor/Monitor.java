package org.example.interfaceAdapters.monitor;

import org.example.entities.Player;
import java.util.List;

public abstract class Monitor {

    abstract void addPlayer(Player player, String command);

    protected abstract void runGameIfPlayersEquals(int MAX_PLAYERS, Player player, String command);

    abstract List<Player> getPlayers();

    abstract int getQueueSize();
}
