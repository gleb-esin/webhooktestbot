package org.example.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Suit implements Comparable<Suit> {
    String suit;
    boolean isTrump;

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
        } else if (o == null || getClass() != o.getClass()) {
            return false;
        } else return suit.equals(((Suit) o).suit);
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
