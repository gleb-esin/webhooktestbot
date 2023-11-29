package org.example.controller.move;

import org.example.controller.PlayerController;
import org.example.controller.PlayerInputValidator;
import org.example.controller.TableController;
import org.example.model.Card;
import org.example.model.Player;

import java.util.List;

import static org.example.controller.moveValidator.DefenceValidator.isDefenceCorrect;
import static org.example.service.MessageHandler.*;

public class Defence extends PlayerInputValidator implements MoveInterface {

    public void init(PlayerController playerController, TableController tableController) {
        sendNotificationToAll(playerController.getPlayers(),
                "------------------------------\n" +
                        "Отбивается " + playerController.getDefender().getName() +
                        "\n" + tableController.getTable() +
                        "\n------------------------------");
        sendNotificationTo(playerController.getDefender(), playerController.getDefender().toString());
    }

    public void move(Player defender, List<Player> playersForNotify, TableController tableController) {
        List<Card> unbeatenCards = tableController.getTable().getUnbeatenCards();
        boolean canDefend = isDefenceCorrect(unbeatenCards, defender.getPlayerHand());
        //If defender can't beat attacker cards...
        if (!canDefend) {
            sendNotificationToAll(playersForNotify, defender.getName() + " не может отбиться.");
            //...set his role to binder
            defender.setRole("binder");
            //If defender can beat attacker cards...
        } else {
            //...ask defender for cards.
            List<Card> cards = askForCards(defender);
            //If defender refused to beat cards...
            if (cards.isEmpty()) {
                sendNotificationToAll(playersForNotify, defender.getName() + " не будет отбиваться");
                //...set his role to binder
                defender.setRole("binder");
            //If defender decided to beat cards...
            } else {
                ///...check his cards for correct move
                boolean isDefendPossible = isDefenceCorrect(unbeatenCards, cards);
                while (!isDefendPossible || cards.size() > unbeatenCards.size()) {
                    if (cards.isEmpty()) {
                        defender.setRole("binder");
                        break;
                    }
                    //...and ask defender for correct cards.
                    sendNotificationTo(defender, "Так не получится отбиться");
                    cards = askForCards(defender);
                    isDefendPossible = isDefenceCorrect(unbeatenCards, cards);
                }
                //If defender could beat cards...
                if (!defender.getRole().equals("binder")) {
                    sendNotificationToAll(playersForNotify, defender.getName() + " отбился");
                    //...we add these cards on the table...
                    tableController.addCardsToTable(cards, defender);
                }
            }
        }
    }
}
