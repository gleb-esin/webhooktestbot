package org.example.BusinessLayer.move;

import org.example.BusinessLayer.controller.PlayerController;
import org.example.BusinessLayer.controller.TableController;
import org.example.EntityLayer.Card;
import org.example.EntityLayer.Player;
import org.example.ServiseLayer.services.MessageService;
import org.example.ServiseLayer.services.PlayerInputValidator;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DefenceForTransferFool extends AbstractDefence implements Defence {
    public DefenceForTransferFool(MessageService messageService, PlayerInputValidator playerInputValidator) {
        super(messageService, playerInputValidator);
    }

    @Override
    public void move(PlayerController playerController, TableController tableController) {
        Player defender = playerController.getDefender();
        List<Player> playersForNotify = playerController.getPlayers();
        List<Card> unbeatenCards = tableController.getTable().getUnbeatenCards();
        boolean isDefencePossible = isDefenceCorrect(unbeatenCards, defender.getPlayerHand());
        boolean isTransferPossible = isTransferPossible(unbeatenCards, defender.getPlayerHand());
        boolean isMovePossible = isDefencePossible || isTransferPossible;
        //If defender can't beat attacker cards...
        if (!isMovePossible) {
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
                boolean isDefendCorrect = isDefenceCorrect(unbeatenCards, cards);
                boolean isTransferCorrect = isTransferPossible(unbeatenCards, cards);
                boolean isMoveCorrect = isDefendCorrect || isTransferCorrect;
                while (!isMoveCorrect || cards.size() > unbeatenCards.size()) {
                    if (cards.isEmpty()) {
                        defender.setRole("binder");
                        break;
                    }
                    //...and ask defender for correct cards.
                    messageService.sendMessageTo(defender, "Так не получится отбиться");
                    cards = playerInputValidator.askForCards(defender);
                    isDefendCorrect = isDefenceCorrect(unbeatenCards, cards);
                    isTransferCorrect = isTransferCorrect(unbeatenCards, cards);
                    isMoveCorrect = isDefendCorrect || isTransferCorrect;
                }
                if (isTransferCorrect) {
                    defender.setRole("carsTransfer");
                }
                if (!defender.getRole().equals("binder")) {
                    tableController.addCardsToTable(cards, defender);
                    messageService.sendMessageToAll(playersForNotify, tableController.getTable().toString());
                    if (defender.getRole().equals("carsTransfer")) {
                        messageService.sendMessageToAll(playersForNotify, defender.getName() + " перевел карты!");
                    } else {
                        messageService.sendMessageToAll(playersForNotify, "Карты отбиты!");
                    }
                }
            }
        }
        if (defender.getRole().equals("binder")) playerController.setBinder(defender);
    }

    protected boolean isTransferPossible(List<Card> unbeatenCards, List<Card> defendersHand) {
        if (unbeatenCards.size() > 1) {
            Card previousCard = unbeatenCards.get(0);
            for (int i = 1; i < unbeatenCards.size(); i++) {
                if (!previousCard.getValue().equals(unbeatenCards.get(i).getValue())) {
                    return false;
                }
                previousCard = unbeatenCards.get(i);
            }
        }
        for (Card defendersCard : defendersHand) {
            if (unbeatenCards.get(0).getValue().equals(defendersCard.getValue())) {
                return true;
            }
        }
        return false;
    }

    protected boolean isTransferCorrect(List<Card> unbeatenCards, List<Card> cards) {
        for (Card card : cards) {
            if (!unbeatenCards.get(0).getValue().equals(card.getValue())) {
                return false;
            }
        }
        return true;
    }
}
