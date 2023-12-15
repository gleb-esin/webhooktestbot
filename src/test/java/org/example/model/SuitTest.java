package org.example.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SuitTest {

    @Test
    void testEquals() {
        Suit suitSpades1 = new Suit("♠", true);
        Suit suitSpades2 = new Suit("♠", true);
        Suit suitDiamonds = new Suit("♦", false);

        assertFalse(suitSpades1.equals(suitDiamonds));
        assertTrue(suitSpades1.equals(suitSpades2));
    }

    @Test
    void compareTo() {
        Suit suitSpades1 = new Suit("♠", true);
        Suit suitSpades2 = new Suit("♠", true);
        Suit suitDiamonds = new Suit("♦", false);
        Suit suitHearts = new Suit("♥", false);

        assertEquals(0, suitSpades1.compareTo(suitSpades2));
        assertEquals(1, suitSpades1.compareTo(suitDiamonds));
        assertEquals(-1, suitDiamonds.compareTo(suitHearts));
    }
}