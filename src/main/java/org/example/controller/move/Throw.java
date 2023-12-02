package org.example.controller.move;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.controller.PlayerInputValidator;
import org.example.controller.TableController;
import org.example.model.Card;
import org.example.model.Player;
import org.example.network.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.example.controller.moveValidator.ThrowValidator.isThrowMoveCorrect;
@Component
@AllArgsConstructor(onConstructor_ = {@Autowired})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Throw extends PlayerInputValidator implements MoveInterface {
    TelegramBot bot;

    @Override
    public void move(Player thrower, List<Player> playersForNotify, TableController tableController) {
        bot.sendNotificationTo(thrower, thrower.toString());
        List<Card> cards = askForCards(thrower);
        if (cards.isEmpty()) {
            bot.sendNotificationToAll(playersForNotify, thrower.getName() + ", не будет подкидывать.");
        } else {
            boolean isThrowCorrect = isThrowMoveCorrect(tableController.getAll(), cards);
            while (!isThrowCorrect) {
                bot.sendNotificationTo(thrower, thrower.getName() + " , так не получится подкинуть.");
                cards = askForCards(thrower);
                isThrowCorrect = isThrowMoveCorrect(tableController.getAll(), cards);
            }
            tableController.addCardsToTable(cards, thrower);
        }
    }
}
