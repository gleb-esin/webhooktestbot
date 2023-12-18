package org.example.model;

import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class Table {
    List<Card> beatenCards = new ArrayList<>();
    List<Card> unbeatenCards = new ArrayList<>();
    Suit trump;

    public Table(Suit trump) {
        this.trump = trump;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<b>Карты на столе:</b>" ).append(System.lineSeparator());
        stringBuilder.append("Отбитые карты: " );
        for (Card c : beatenCards) {
            stringBuilder.append(c).append(" ");
        }
        stringBuilder.append(System.lineSeparator()).append("Неотбитые карты: ");
        for (Card c : unbeatenCards) {
            stringBuilder.append(c);
        }
        stringBuilder.append(System.lineSeparator()).append("Козырь ").append("<b>[").append(trump).append("]</b>");
        return stringBuilder.toString();
    }
    public void setBeatenCard(Card beatenCard) {
        beatenCards.add(beatenCard);
    }

    public void setUnbeatenCard(Card unbeatenCard) {
        unbeatenCards.add(unbeatenCard);
    }
}
