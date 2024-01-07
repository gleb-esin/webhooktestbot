package org.example.BusinessLayer.move;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
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
public class Throw {
    MessageService messageService;
    PlayerInputValidator playerInputValidator;



    public void throwMove(Player thrower, List<Player> playersForNotify, TableController tableController, Player defender) {
        messageService.sendMessageTo(thrower, thrower.toString());
        List<Card> cards = playerInputValidator.askForCards(thrower);
        if (cards.isEmpty()) {
            messageService.sendMessageToAll(playersForNotify, thrower.getName() + " не будет подкидывать.");
        } else {
            boolean isThrowCorrect = isThrowMoveCorrect(tableController.getAll(), cards, defender);
            while (!isThrowCorrect) {
                messageService.sendMessageTo(thrower, thrower.getName() + " , так не получится подкинуть.");
                cards = playerInputValidator.askForCards(thrower);
                isThrowCorrect = isThrowMoveCorrect(tableController.getAll(), cards, defender);
            }
            tableController.addCardsToTable(cards, thrower);
            messageService.sendMessageToAll(playersForNotify, tableController.getTable().toString());

        }
    }

    public boolean isThrowMoveCorrect(List<Card> tableCards, List<Card> throwerCards, Player defender) {
        boolean isThrowCorrect;
        int thrownCards = throwerCards.size();
        if (thrownCards>defender.getPlayerHand().size()) return false;
        int allowedCards = 0;
        for (Card throwerCard : throwerCards) {
            for (Card tableCard : tableCards) {
                isThrowCorrect = tableCard.getValue().equals(throwerCard.getValue());
                if (isThrowCorrect) {
                    allowedCards++;
                    break;
                }
            }
            if (thrownCards == allowedCards) break;
        }
        return thrownCards == allowedCards;
    }

    public boolean isThrowPossible(List<Card> tableCards, List<Card> throwerHand) {
        boolean isThrowPossible = false;
        for (Card tableCard : tableCards) {
            for (Card throwerCard : throwerHand) {
                if (tableCard.getValue().equals(throwerCard.getValue())) {
                    isThrowPossible = true;
                    break;
                }
            }
            if (isThrowPossible) break;
        }
        return isThrowPossible;
    }
}