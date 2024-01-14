package org.example.BusinessLayer.move;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.example.BusinessLayer.controller.DeckController;
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
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Throw {
    MessageService messageService;
    PlayerInputValidator playerInputValidator;
    @NonFinal boolean throwerWantThrow = true;
    @Autowired
    public Throw(MessageService messageService, PlayerInputValidator playerInputValidator) {
        this.messageService = messageService;
        this.playerInputValidator = playerInputValidator;
    }

    public boolean move(Player thrower, PlayerController playerController, TableController tableController, DeckController deckController) {
        Player defender = playerController.getDefender();
        List<Player> playersForNotify = playerController.getPlayers();
        boolean isDefenceNeeded = false;

        for (int i = 0; i < 1; i++) {
            int numberOfUnbeatenCards = tableController.getTable().getUnbeatenCards().size();
            // If thrower can throw send initial notification to all waitingPlayers...
            messageService.sendMessageToAll(playersForNotify, "⚔️ " + thrower.getName() + " может подкинуть ⚔️");
            ///...and make a throw attackMove.
            messageService.sendMessageTo(thrower, thrower.toString());
            List<Card> cards = playerInputValidator.askForCards(thrower);
            if (cards.isEmpty()) {
                messageService.sendMessageToAll(playersForNotify, thrower.getName() + " не будет подкидывать.");
                throwerWantThrow = false;
            } else {
                boolean throwIsNotCorrect = !isThrowMoveCorrect(tableController.getAll(), cards, defender);
                while (throwIsNotCorrect) {
                    messageService.sendMessageTo(thrower, thrower.getName() + " , так не получится подкинуть.");
                    cards = playerInputValidator.askForCards(thrower);
                    throwIsNotCorrect = !isThrowMoveCorrect(tableController.getAll(), cards, defender);
                }
                tableController.addCardsToTable(cards, thrower);
                messageService.sendMessageToAll(playersForNotify, tableController.getTable().toString());
            }
            //If thrower became the winner - break game loop
            if (playerController.isPlayerWinner(thrower, deckController.getDeck())) break;
            // if the thrower still didn't throw...
            boolean throwerDidntThrow = numberOfUnbeatenCards == tableController.getTable().getUnbeatenCards().size();
            //...break throw loop
            if (throwerDidntThrow) {
                break;
            } else {
                //...and defender are not binder...
                if (!defender.getRole().equals("binder")) {
                    //...make a defence move.
                    isDefenceNeeded = true;
                }
            }
        }
        return isDefenceNeeded;
    }

    private boolean isThrowMoveCorrect(List<Card> tableCards, List<Card> throwerCards, Player defender) {
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

    public boolean isThrowPossible(List<Card> tableCards, Player thrower, Player defender) {
        List<Card> throwerHand = thrower.getPlayerHand();
        if (isThrowerReallyWantThrow()) {
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
            return isThrowPossible && !defender.getPlayerHand().isEmpty();
        } else {
            return false;
        }
    }

    private boolean isThrowerReallyWantThrow() {
        if(!throwerWantThrow) {
            throwerWantThrow = true;
            return false;
        } else return true;
    }
}
