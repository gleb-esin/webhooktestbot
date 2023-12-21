package org.example.move;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.network.TelegramBot;
import org.example.controller.TableController;
import org.example.model.Card;
import org.example.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.example.move.moveValidator.ThrowValidator.isThrowMoveCorrect;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Throw implements Move {
    TelegramBot bot;



    public void throwMove(Player thrower, List<Player> playersForNotify, TableController tableController) {
        sendMessageTo(thrower, thrower.toString(), bot);
        List<Card> cards = askForCards(thrower, bot);

        if (cards.isEmpty()) {
            sendMessageToAll(playersForNotify, thrower.getName() + " не будет подкидывать.", bot);
        } else {
            boolean isThrowCorrect = isThrowMoveCorrect(tableController.getAll(), cards);
            while (!isThrowCorrect) {
                sendMessageTo(thrower, thrower.getName() + " , так не получится подкинуть.", bot);
                cards = askForCards(thrower, bot);
                isThrowCorrect = isThrowMoveCorrect(tableController.getAll(), cards);
            }
            tableController.addCardsToTable(cards, thrower);
            sendMessageToAll(playersForNotify, tableController.getTable().toString(), bot);

        }
    }
}
