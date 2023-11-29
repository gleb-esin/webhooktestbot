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
        stringBuilder.append("\nКозырь ");
        if (trump.getSuit().equals("♥") || trump.getSuit().equals("♦")) {
            stringBuilder.append( "\u001B[38;2;128;0;0;47m" + trump + "\u001B[0m");
        } else {
            stringBuilder.append("\u001B[30;47m" + trump + "\u001B[0m");
        }
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
