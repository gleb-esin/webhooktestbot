package org.example.ServiseLayer.factories;

import org.example.EntityLayer.Player;

import java.util.List;
import java.util.UUID;

public interface GameBuilder {
    void runGame(List<Player> players);

    void finnishGame(List<Player> players, UUID gameID);

    void buildGame(List<Player> players);
}
