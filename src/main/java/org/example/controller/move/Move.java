package org.example.controller.move;

import org.example.controller.PlayerController;
import org.example.controller.TableController;
import org.example.model.Player;
import org.example.network.TelegramBot;

import java.util.List;

public interface Move extends Attack, Defence, Throw {

    @Override
    default void attackInit(TelegramBot bot, PlayerController playerController, TableController tableController) {
        Attack.super.attackInit(bot, playerController, tableController);
    }

    @Override
    default void attackMove(TelegramBot bot, Player attacker, TableController tableController) {
        Attack.super.attackMove(bot, attacker,  tableController);
    }

    @Override
    default void defenceInit(TelegramBot bot, PlayerController playerController, TableController tableController) {
        Defence.super.defenceInit(bot, playerController, tableController);
    }

    @Override
    default void defenceMove(TelegramBot bot, Player defender, List<Player> playersForNotify, TableController tableController) {
        Defence.super.defenceMove(bot, defender, playersForNotify, tableController);
    }

    @Override
    default void throwMove(TelegramBot bot, Player thrower, List<Player> playersForNotify, TableController tableController) {
        Throw.super.throwMove(bot, thrower, playersForNotify, tableController);
    }
}
