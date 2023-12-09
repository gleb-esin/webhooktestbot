package org.example.controller.move;

import org.example.controller.PlayerController;
import org.example.controller.PlayerInputValidator;
import org.example.controller.TableController;
import org.example.model.Card;
import org.example.model.Player;
import org.example.network.TelegramBot;
import org.example.service.MessageHandler;

import java.util.List;

import static org.example.controller.moveValidator.DefenceValidator.isDefenceCorrect;

public interface Defence extends PlayerInputValidator, MessageHandler {

    default void defenceInit(TelegramBot bot, PlayerController playerController, TableController tableController) {
        sendMessageToAll(playerController.getPlayers(),
                "------------------------------\n" +
                        "Отбивается " + playerController.getDefender().getName() +
                        "\n" + tableController.getTable() +
                        "\n------------------------------");
        sendMessageTo(playerController.getDefender(), playerController.getDefender().toString());
    }

    default void defenceMove(TelegramBot bot, Player defender, List<Player> playersForNotify, TableController tableController) {
        List<Card> unbeatenCards = tableController.getTable().getUnbeatenCards();
        boolean canDefend = isDefenceCorrect(unbeatenCards, defender.getPlayerHand());
        //If defender can't beat attacker cards...
        if (!canDefend) {
            sendMessageToAll(playersForNotify, defender.getName() + " не может отбиться.");
            //...set his role to binder
            defender.setRole("binder");
            //If defender can beat attacker cards...
        } else {
            //...ask defender for cards.
            List<Card> cards = askForCards(defender);
            //If defender refused to beat cards...
            if (cards.isEmpty()) {
                sendMessageToAll(playersForNotify, defender.getName() + " не будет отбиваться");
                //...set his role to binder
                defender.setRole("binder");
                //If defender decided to beat cards...
            } else {
                ///...check his cards for correct attackMove
                boolean isDefendPossible = isDefenceCorrect(unbeatenCards, cards);
                while (!isDefendPossible || cards.size() > unbeatenCards.size()) {
                    if (cards.isEmpty()) {
                        defender.setRole("binder");
                        break;
                    }
                    //...and ask defender for correct cards.
                    sendMessageTo(defender, "Так не получится отбиться");
                    cards = askForCards(defender);
                    isDefendPossible = isDefenceCorrect(unbeatenCards, cards);
                }
                //If defender could beat cards...
                if (!defender.getRole().equals("binder")) {
                    tableController.addCardsToTable(cards, defender);
                    sendMessageToAll(playersForNotify, tableController.getTable().toString());
                    sendMessageToAll(playersForNotify, "Карты отбиты!");
                    //...we add these cards on the table...
                }
            }
        }
    }
}
