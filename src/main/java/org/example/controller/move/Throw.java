package org.example.controller.move;

import org.example.controller.PlayerInputValidator;
import org.example.controller.TableController;
import org.example.model.Card;
import org.example.model.Player;
import org.example.network.TelegramBot;
import org.example.service.MessageHandler;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.example.controller.moveValidator.ThrowValidator.isThrowMoveCorrect;

@Component

public interface Throw extends PlayerInputValidator, MessageHandler {


    default void throwMove(TelegramBot bot, Player thrower, List<Player> playersForNotify, TableController tableController) {
        sendMessageTo(bot, thrower, thrower.toString());
        List<Card> cards = askForCards(bot, thrower);

        if (cards.isEmpty()) {
            sendMessageToAll(bot, playersForNotify, thrower.getName() + ", не будет подкидывать.");
        } else {
            boolean isThrowCorrect = isThrowMoveCorrect(tableController.getAll(), cards);
            while (!isThrowCorrect) {
                sendMessageTo(bot, thrower, thrower.getName() + " , так не получится подкинуть.");
                cards = askForCards(bot, thrower);
                isThrowCorrect = isThrowMoveCorrect(tableController.getAll(), cards);
            }
            tableController.addCardsToTable(cards, thrower);
        }
    }
}
