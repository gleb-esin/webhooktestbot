package org.example.BusinessLayer.move;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.BusinessLayer.controller.PlayerController;
import org.example.BusinessLayer.controller.TableController;
import org.example.EntityLayer.Card;
import org.example.EntityLayer.Player;
import org.example.ServiseLayer.services.MessageService;
import org.example.ServiseLayer.services.PlayerInputValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Defence  {
    MessageService messageService;
    PlayerInputValidator playerInputValidator;

    public void init(PlayerController playerController) {
        messageService.sendMessageToAll(playerController.getPlayers(),
                "\uD83D\uDEE1 Отбивается " + playerController.getDefender().getName() + " \uD83D\uDEE1");
        messageService.sendMessageTo(playerController.getDefender(), playerController.getDefender().toString());
    }

    public void move(PlayerController playerController, TableController tableController) {
        Player defender = playerController.getDefender();
        List<Player> playersForNotify = playerController.getPlayers();
        List<Card> unbeatenCards = tableController.getTable().getUnbeatenCards();
        boolean canDefend = isDefenceCorrect(unbeatenCards, defender.getPlayerHand());
        //If defender can't beat attacker cards...
        if (!canDefend) {
            messageService.sendMessageToAll(playersForNotify, tableController.getTable().toString());
            messageService.sendMessageToAll(playersForNotify, defender.getName() + " не может отбиться.");
            //...set his role to binder
            defender.setRole("binder");
            //If defender can beat attacker cards...
        } else {
            //...ask defender for cards.
            List<Card> cards = playerInputValidator.askForCards(defender);
            //If defender refused to beat cards...
            if (cards.isEmpty()) {
                messageService.sendMessageToAll(playersForNotify, tableController.getTable().toString());
                messageService.sendMessageToAll(playersForNotify, defender.getName() + " не будет отбиваться");
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
                    messageService.sendMessageTo(defender, "Так не получится отбиться");
                    cards = playerInputValidator.askForCards(defender);
                    isDefendPossible = isDefenceCorrect(unbeatenCards, cards);
                }
                //If defender could beat cards...
                if (!defender.getRole().equals("binder")) {
                    tableController.addCardsToTable(cards, defender);
                    messageService.sendMessageToAll(playersForNotify, tableController.getTable().toString());
                    messageService.sendMessageToAll(playersForNotify, "Карты отбиты!");
                    //...we add these cards on the table...
                }
            }
        }
        if (defender.getRole().equals("binder")) playerController.setBinder(defender);
    }

    private boolean isDefenceCorrect(List<Card> unbeatenCards, List<Card> defenderCards) {
        int isDefendCorrect;
        int cardsNumberToBeat = unbeatenCards.size();
        int beatenCards = 0;
        for (Card tableCard : unbeatenCards) {
            for (Card defenderCard : defenderCards) {
                isDefendCorrect = defenderCard.compareTo(tableCard);
                if (isDefendCorrect > 0) {
                    beatenCards++;
                    break;
                }
            }
            if (beatenCards == cardsNumberToBeat) break;
        }
        return beatenCards == cardsNumberToBeat;

    }
}
