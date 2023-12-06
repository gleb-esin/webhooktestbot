package org.example.controller.move;

import org.example.controller.PlayerController;
import org.example.controller.PlayerInputValidator;
import org.example.controller.TableController;
import org.example.model.Card;
import org.example.model.Player;
import org.example.network.TelegramBot;
import org.example.service.MessageHandler;

import java.util.List;

import static org.example.controller.moveValidator.AttackValidator.isAttackMoveCorrect;

public interface Attack extends PlayerInputValidator, MessageHandler {

    default void attackInit(TelegramBot bot, PlayerController playerController, TableController tableController) {
        Player attacker = playerController.getAttacker();
        Player defender = playerController.getDefender();
        sendMessageToAll(bot, playerController.getPlayers(), "==============================\n" +
                "Ход " + attacker.getName() + " под " + defender.getName()+
                "\n"+tableController.getTable());
        sendMessageTo(bot, attacker, attacker.toString());
    }

    default void attackMove(TelegramBot bot,Player attacker, TableController tableController) {
        List<Card> cards = askForCards(bot, attacker);
        boolean isMoveCorrect = isAttackMoveCorrect(cards);
        while (cards.isEmpty() || (cards.size() > 1 && !isMoveCorrect)) {
            sendMessageTo(bot, attacker, "Так пойти не получится.");
            cards = askForCards(bot, attacker);
            isMoveCorrect = isAttackMoveCorrect(cards);
        }
        tableController.addCardsToTable(cards, attacker);
        attacker.setRole("thrower");
    }
}
