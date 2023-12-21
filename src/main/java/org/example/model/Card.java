package org.example.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class Card implements Comparable<Card> {
    Suit suit;
    Value value;
    Integer weight;

    public Card(String suit, String value, boolean trump) {
        this.suit = new Suit(suit, trump);
        this.value = new Value(value);
        if (trump) {
            this.weight = this.value.getWeight() + 100;
        } else this.weight = this.value.getWeight();
    }

    /**
     * If a card can't beat another card, method returns -1
     */
    @Override
    public int compareTo(@NonNull Card o) {
        if (suit.equals(o.suit)) {
            return Integer.compare(weight, o.weight);
        } else if (suit.isTrump() && !o.suit.isTrump()) {
            return 1;
        } else return -1;
    }


    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(suit, card.suit) &&
                Objects.equals(value, card.value) &&
                weight == card.weight;
    }
    @Override
    public String toString() {
        return new StringBuilder("<b>[").append(value).append(suit).append("]</b>").toString();
    }
}



