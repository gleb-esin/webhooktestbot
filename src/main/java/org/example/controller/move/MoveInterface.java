package org.example.controller.move;

import org.example.controller.TableController;
import org.example.model.Player;
import java.util.List;

public interface MoveInterface {
    void move(Player player, List<Player> playersForNotify, TableController tableController);

}
