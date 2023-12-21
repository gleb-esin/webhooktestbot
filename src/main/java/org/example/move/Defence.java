package org.example.move;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.controller.PlayerController;
import org.example.network.TelegramBot;
import org.example.controller.TableController;
import org.example.model.Card;
import org.example.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.example.move.moveValidator.DefenceValidator.isDefenceCorrect;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Defence implements Move {
    TelegramBot bot;

    public void init(PlayerController playerController) {
        sendMessageToAll(playerController.getPlayers(),
                "\uD83D\uDEE1 Отбивается " + playerController.getDefender().getName() + " \uD83D\uDEE1", bot);
        sendMessageTo(playerController.getDefender(), playerController.getDefender().toString(), bot);
    }

    public void move(Player defender, List<Player> playersForNotify, TableController tableController) {
        List<Card> unbeatenCards = tableController.getTable().getUnbeatenCards();
        boolean canDefend = isDefenceCorrect(unbeatenCards, defender.getPlayerHand());
        //If defender can't beat attacker cards...
        if (!canDefend) {
            sendMessageToAll(playersForNotify, tableController.getTable().toString(), bot);
            sendMessageToAll(playersForNotify, defender.getName() + " не может отбиться.", bot);
            //...set his role to binder
            defender.setRole("binder");
            //If defender can beat attacker cards...
        } else {
            //...ask defender for cards.
            List<Card> cards = askForCards(defender, bot);
            //If defender refused to beat cards...
            if (cards.isEmpty()) {
                sendMessageToAll(playersForNotify, tableController.getTable().toString(), bot);
                sendMessageToAll(playersForNotify, defender.getName() + " не будет отбиваться", bot);
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
                    sendMessageTo(defender, "Так не получится отбиться", bot);
                    cards = askForCards(defender, bot);
                    isDefendPossible = isDefenceCorrect(unbeatenCards, cards);
                }
                //If defender could beat cards...
                if (!defender.getRole().equals("binder")) {
                    tableController.addCardsToTable(cards, defender);
                    sendMessageToAll(playersForNotify, tableController.getTable().toString(), bot);
                    sendMessageToAll(playersForNotify, "Карты отбиты!", bot);
                    //...we add these cards on the table...
                }
            }
        }
    }
}
