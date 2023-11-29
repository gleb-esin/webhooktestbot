package org.example.controller.move;


import org.example.controller.PlayerController;
import org.example.controller.PlayerInputValidator;
import org.example.controller.TableController;
import org.example.model.Card;
import org.example.model.Player;

import java.util.List;

import static org.example.controller.moveValidator.AttackValidator.isAttackMoveCorrect;
import static org.example.service.MessageHandler.sendNotificationTo;
import static org.example.service.MessageHandler.sendNotificationToAll;

public class Attack extends PlayerInputValidator implements MoveInterface {

    public void init(PlayerController playerController, TableController tableController) {
        Player attacker = playerController.getAttacker();
        Player defender = playerController.getDefender();
        sendNotificationToAll(playerController.getPlayers(), "==============================\n" +
                "Ход " + attacker.getName() + " под " + defender.getName()+
                "\n"+tableController.getTable());
        sendNotificationTo(attacker, attacker.toString());
    }
    @Override
    public void move(Player attacker, List<Player> playersForNotify, TableController tableController) {
        List<Card> cards = askForCards(attacker);
        boolean isMoveCorrect = isAttackMoveCorrect(cards);
        while (cards.isEmpty() || (cards.size() > 1 && !isMoveCorrect)) {
            sendNotificationTo(attacker, "Так пойти не получится.");
            cards = askForCards(attacker);
            isMoveCorrect = isAttackMoveCorrect(cards);
        }
        tableController.addCardsToTable(cards, attacker);
        attacker.setRole("thrower");
    }
}
