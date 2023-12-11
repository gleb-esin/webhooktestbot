package org.example.controller.move;

import org.example.controller.PlayerController;
import org.example.controller.TableController;
import org.example.model.Player;

import java.util.List;

public interface Move extends Attack, Defence, Throw {

    @Override
    default void attackInit(PlayerController playerController, TableController tableController) {
        Attack.super.attackInit(playerController, tableController);
    }

    @Override
    default void attackMove(Player attacker, TableController tableController, PlayerController playerController) {
        Attack.super.attackMove(attacker,  tableController, playerController);
    }

    @Override
    default void defenceInit(PlayerController playerController, TableController tableController) {
        Defence.super.defenceInit(playerController, tableController);
    }

    @Override
    default void defenceMove(Player defender, List<Player> playersForNotify, TableController tableController) {
        Defence.super.defenceMove(defender, playersForNotify, tableController);
    }

    @Override
    default void throwMove(Player thrower, List<Player> playersForNotify, TableController tableController) {
        Throw.super.throwMove(thrower, playersForNotify, tableController);
    }
}
