package org.example.interfaceAdapters.factories;

import org.example.entities.Player;

import java.util.List;
import java.util.UUID;

public interface GameFactory {
    void create(List<Player> players);

    void finnish(List<Player> players, UUID gameID);
}
