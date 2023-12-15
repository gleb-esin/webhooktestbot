package org.example.move.moveValidator;

import org.example.model.Card;

import java.util.List;

public class AttackValidator {
    public static boolean isAttackMoveCorrect(List<Card> cards) {
        boolean isMoveCorrect = true;
        for (int i = 0; i < cards.size() - 1; i++) {
            isMoveCorrect = cards.get(i).getValue().equals(cards.get(i + 1).getValue());
            if(!isMoveCorrect) break;
        }
        return isMoveCorrect;
    }
}
