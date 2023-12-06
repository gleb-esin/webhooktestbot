package org.example.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Table {
    private final List<Card> beatenCards = new ArrayList<>();
    private final List<Card> unbeatenCards = new ArrayList<>();
    private Suit trump;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Отбитые карты: " );
        for (Card c : beatenCards) {
            stringBuilder.append(c).append(" ");
        }
        stringBuilder.append(System.lineSeparator()).append("Неотбитые карты: ");
        for (Card c : unbeatenCards) {
            stringBuilder.append(c).append(" ");
        }
        stringBuilder.append("\nКозырь " + trump);
        return stringBuilder.toString();
    }

    public void setBeatenCard(Card beatenCard) {
        beatenCards.add(beatenCard);
    }

    public void setUnbeatenCard(Card unbeatenCard) {
        unbeatenCards.add(unbeatenCard);
    }

    public void setTrump(Suit trump) {
        this.trump = trump;
    }
}
