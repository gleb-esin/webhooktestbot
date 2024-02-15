package org.example.BusinessLayer.move;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.BusinessLayer.controller.PlayerController;
import org.example.BusinessLayer.controller.TableController;
import org.example.EntityLayer.Card;
import org.example.ServiseLayer.services.MessageService;
import org.example.ServiseLayer.services.PlayerInputValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class AbstractDefence implements Defence {
    MessageService messageService;
    PlayerInputValidator playerInputValidator;

    public void init(PlayerController playerController) {
        messageService.sendMessageToAll(playerController.getPlayers(),
                "\uD83D\uDEE1 Отбивается " + playerController.getDefender().getName() + " \uD83D\uDEE1");
        messageService.sendMessageTo(playerController.getDefender(), playerController.getDefender().toString());
    }

    public abstract void move(PlayerController playerController, TableController tableController);

    protected boolean isDefenceCorrect(List<Card> unbeatenCards, List<Card> defenderCards) {
        boolean isDefendCorrect;
        int cardsNumberToBeat = unbeatenCards.size();
        int beatenCards = 0;
        for (Card defenderCard : defenderCards) {
            for (Card unbeatenCard : unbeatenCards) {
                isDefendCorrect = defenderCard.compareTo(unbeatenCard) > 0;
                if (isDefendCorrect) {
                    beatenCards++;
                    break;
                }
            }
            if (beatenCards == cardsNumberToBeat) break;
        }
        return beatenCards == cardsNumberToBeat;
    }
}
