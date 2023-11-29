package org.example.controller.moveValidator;

import org.example.model.Card;

import java.util.List;

public class DefenceValidator {
    public static boolean isDefenceCorrect(List<Card> unbeatenCards, List<Card> defenderCards) {
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
