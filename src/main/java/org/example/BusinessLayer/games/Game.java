package org.example.BusinessLayer.games;

import org.example.EntityLayer.Player;

import java.util.List;

public interface Game {
    void play(List<Player> players);
}
