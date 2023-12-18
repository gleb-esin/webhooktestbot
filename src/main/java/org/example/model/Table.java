package org.example.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Table {
    private final List<Card> beatenCards = new ArrayList<>();
    private final List<Card> unbeatenCards = new ArrayList<>();
    private Suit trump;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<b>Карты на столе:</b>\n" );
        stringBuilder.append("Отбитые карты: " );
        for (Card c : beatenCards) {
            stringBuilder.append(c).append(" ");
        }
        stringBuilder.append(System.lineSeparator()).append("Неотбитые карты: ");
        for (Card c : unbeatenCards) {
            stringBuilder.append(c);
        }
        stringBuilder.append("\nКозырь ").append("<b>[").append(trump).append("]</b>");
        return stringBuilder.toString();
    }
    public void setBeatenCard(Card beatenCard) {
        beatenCards.add(beatenCard);
    }

    public void setUnbeatenCard(Card unbeatenCard) {
        unbeatenCards.add(unbeatenCard);
    }

}
