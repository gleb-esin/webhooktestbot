package org.example.model;

import lombok.Data;

@Data
public class Suit implements Comparable<Suit> {
    private  String suit;
    private  boolean isTrump;

    public Suit(String suit, boolean trump) {
       this.suit = suit;
       this.isTrump = trump;
    }

    @Override
    public String toString() {
        return suit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Suit objSuit = (Suit) o;
        return suit.equals(objSuit.suit);
    }

    @Override
    public int compareTo(Suit o) {
        if (suit.equals(o.suit)) {
            return 0;
        } else {
            if (isTrump && !o.isTrump) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}
