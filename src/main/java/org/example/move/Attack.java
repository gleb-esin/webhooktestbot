package org.example.move;

import org.example.controller.PlayerController;
import org.example.service.PlayerInputValidator;
import org.example.controller.TableController;
import org.example.model.Card;
import org.example.model.Player;
import org.example.service.MessageService;

import java.util.List;

import static org.example.move.moveValidator.AttackValidator.isAttackMoveCorrect;

public interface Attack extends PlayerInputValidator, MessageService {

    default void attackInit(PlayerController playerController, TableController tableController) {
        Player attacker = playerController.getAttacker();
        Player defender = playerController.getDefender();
        sendMessageToAll(playerController.getPlayers(), "⚔️ Ход " + attacker.getName() + " под " + defender.getName()+ "⚔️" +
                "\n" + tableController.getTable().toString());
        sendMessageTo(attacker, attacker.toString());
    }

    default void attackMove(Player attacker, TableController tableController, PlayerController playerController) {
        List<Card> cards = askForCards(attacker);
        boolean isMoveCorrect = isAttackMoveCorrect(cards);
        while (cards.isEmpty() || (cards.size() > 1 && !isMoveCorrect)) {
            sendMessageTo(attacker, "Так пойти не получится.");
            cards = askForCards(attacker);
            isMoveCorrect = isAttackMoveCorrect(cards);
        }
        tableController.addCardsToTable(cards, attacker);
        sendMessageToAll(playerController.getPlayers(), tableController.getTable().toString());
        attacker.setRole("thrower");
    }
}
