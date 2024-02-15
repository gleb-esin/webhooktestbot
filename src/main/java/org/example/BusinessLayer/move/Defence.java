package org.example.BusinessLayer.move;

import org.example.BusinessLayer.controller.PlayerController;
import org.example.BusinessLayer.controller.TableController;

public interface Defence {
    void init(PlayerController playerController);

    void move(PlayerController playerController, TableController tableController);
}
