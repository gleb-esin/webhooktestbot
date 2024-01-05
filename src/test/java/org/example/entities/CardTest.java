package org.example.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    void compareTo_whenCardsHaveTheSameSuitNotValue_then1() {
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
    void compareTo_whenCardsAreEqual_then0() {
        Card spades71 = new Card("♠", "7", true);
        Card spades7 = new Card("♠", "7", true);

        assertEquals(0, spades71.compareTo(spades7));
    }

    @Test
    void testEquals_whenCardsAreEqual_thenTrue() {
        Card heartsJ = new Card("♥", "J", false);
        Card heartsJ2 = new Card("♥", "J", false);

        assertEquals(heartsJ, heartsJ2);
    }

    @Test
    void testEquals_whenCardsAreNotEqual_thenNotEqual() {
        Card heartsJ = new Card("♥", "J", false);
        Card heartsK = new Card("♥", "K", false);

        assertNotEquals(heartsJ, heartsK);
    }

    @Test
    void testEquals_whenOneCardIsANull_thenNotEqual() {
        Card heartsJ = new Card("♥", "J", false);
        Card heartsK = null;

        assertNotEquals(heartsJ, heartsK);
    }

    @Test
    void testEquals_whenOneCardsAreNotCardClass_thenNotEqual() {
        Card heartsJ = new Card("♥", "J", false);
        String heartsK = "K♥";

        assertNotEquals(heartsJ, heartsK);
    }


    @Test
    void testToString() {
        Card heartsJ = new Card("♥", "J", false);

        assertEquals("<b>[ J♥]</b>", heartsJ.toString());
    }


}