package org.example.model;

import lombok.Data;

@Data
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


    public Card() {
    }

    @Override
    public int compareTo(Card o) {
        if ((this.suit.isTrump()) && (!o.suit.isTrump())) return 1;
        if ((!this.suit.isTrump()) && (o.suit.isTrump())) return -1;
        if (this.suit.equals(o.suit)) {
            return Integer.compare(weight, o.weight);
        } else return -1;
    }

    @Override
    public String toString() {
        if (suit.getSuit().equals("\u2665") || suit.getSuit().equals("\u2666")) {
            return "\u001B[38;2;128;0;0;47m" + value + suit + "\u001B[0m";
        } else {
            return "\u001B[30;47m" + value + suit + "\u001B[0m";
        }
    }
}



