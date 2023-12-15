package org.example.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
public class Card implements Comparable<Card> {
    private Suit suit;
    private Value value;
    private Integer weight;

    public Card(String suit, String value, boolean trump) {
        this.suit = new Suit(suit, trump);
        this.value = new Value(value);
        if (trump) {
            this.weight = this.value.getWeight() + 100;
        } else this.weight = this.value.getWeight();
    }

    /**
     * If a card can't beat another card, method returns -1*/
    @Override
    public int compareTo(@NonNull Card o) {
        if ((this.suit.isTrump()) && (!o.suit.isTrump())) return 1;
        if ((!this.suit.isTrump()) && (o.suit.isTrump())) return -1;
        if (this.suit.equals(o.suit)) {
            return Integer.compare(weight, o.weight);
        } else return -1;
    }

    @Override
    public String toString() {
            return "<b>[" + value +  suit + "]</b>";
    }
}



