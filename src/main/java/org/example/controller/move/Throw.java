package org.example.controller.move;

import org.example.controller.PlayerInputValidator;
import org.example.controller.TableController;
import org.example.model.Card;
import org.example.model.Player;

import java.util.List;

import static org.example.controller.moveValidator.ThrowValidator.isThrowMoveCorrect;
import static org.example.service.MessageHandler.sendNotificationTo;
import static org.example.service.MessageHandler.sendNotificationToAll;


public class Throw extends PlayerInputValidator implements MoveInterface {

    @Override
    public void move(Player thrower, List<Player> playersForNotify, TableController tableController) {
        sendNotificationTo(thrower, thrower.toString());
        List<Card> cards = askForCards(thrower);
        if (cards.isEmpty()) {
            sendNotificationToAll(playersForNotify, thrower.getName() + ", не будет подкидывать.");
        } else {
            boolean isThrowCorrect = isThrowMoveCorrect(tableController.getAll(), cards);
            while (!isThrowCorrect) {
                sendNotificationTo(thrower, thrower.getName() + " , так не получится подкинуть.");
                cards = askForCards(thrower);
                isThrowCorrect = isThrowMoveCorrect(tableController.getAll(), cards);
            }
            tableController.addCardsToTable(cards, thrower);
        }
    }
}
