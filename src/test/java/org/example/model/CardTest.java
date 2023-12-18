package org.example.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    void compareTo_whenCardsHaveTheSameSuit_then1() {
        Card spades7 = new Card("♠", "7", true);
        Card spades8 = new Card("♠", "8", true);

        assertEquals(1, spades8.compareTo(spades7));
    }

    @Test
    void compareTo_whenCardsAreDifferentAndNotATrump_thenMinus1() {
        Card heatsA = new Card("♥", "A", false);
        Card diamondsK = new Card("♦", "K", false);

        assertEquals(-1, heatsA.compareTo(diamondsK));
    }

    @Test
    void compareTo_whenFirstOneCardIsATrump_then1() {
        Card spades7 = new Card("♠", "7", true);
        Card heatsA = new Card("♥", "A", false);

      assertEquals(1, spades7.compareTo(heatsA));
    }

    @Test
    void compareTo_whenSecondOneCardIsATrump_thenMinus1() {
        Card spades7 = new Card("♠", "7", true);
        Card diamondsK = new Card("♦", "K", false);

        assertEquals(-1, diamondsK.compareTo(spades7));
    }


    @Test
    void testToString() {
        Card heartsJ = new Card("♥", "J", false);

        assertEquals("<b>[ J♥]</b>", heartsJ.toString());
    }
}