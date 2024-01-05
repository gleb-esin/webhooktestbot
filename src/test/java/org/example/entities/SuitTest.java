package org.example.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SuitTest {

    @Test
    void testEquals_whenSuitsAreTheSame_thenEquals() {
        Suit suitSpades1 = new Suit("♠", true);

        assertEquals(suitSpades1, suitSpades1);
    }

    @Test
    void testEquals_whenSuitsAreEqual_thenEquals() {
        Suit suitSpades1 = new Suit("♠", true);
        Suit suitSpades2 = new Suit("♠", true);

        assertEquals(suitSpades1, suitSpades2);
    }

    @Test
    void testEquals_whenSuitsAreNotEqual_thenNotEquals() {
        Suit suitSpades1 = new Suit("♠", true);
        Suit suitDiamonds = new Suit("♦", false);

        assertNotEquals(suitSpades1, suitDiamonds);
    }

    @Test
    void testEquals_whenOneOfSuitIsNull_thenNotEquals() {
        Suit suitSpades1 = new Suit("♠", true);

        assertFalse(suitSpades1.equals(null));
    }

    @Test
    void testEquals_whenOneOfSuitIsNotCardClass_thenNotEquals() {
        Suit suitSpades1 = new Suit("♠", true);
        String spades = "♠";

        assertNotEquals(spades, suitSpades1);
    }



    @Test
    void compareTo_whenSuitsAreTheSame_then0() {
        Suit suitSpades1 = new Suit("♠", true);
        Suit suitSpades2 = new Suit("♠", true);

        assertEquals(0, suitSpades1.compareTo(suitSpades2));
    }

    @Test
    void compareTo_whenFirstOneSuitIsATrump_then1() {
        Suit suitSpades1 = new Suit("♠", true);
        Suit suitDiamonds = new Suit("♦", false);

        assertEquals(1, suitSpades1.compareTo(suitDiamonds));
    }

    @Test
    void compareTo_whenSecondOneSuitIsATrump_thenMinus1() {
        Suit suitSpades1 = new Suit("♠", true);
        Suit suitHearts = new Suit("♥", false);

        assertEquals(-1, suitHearts.compareTo(suitSpades1));
    }

    @Test
    void compareTo_whenSuitsAreNotEqualAndNotATrump_thenMinus1() {
        Suit suitDiamonds = new Suit("♦", false);
        Suit suitHearts = new Suit("♥", false);

        assertEquals(-1, suitDiamonds.compareTo(suitHearts));
    }
}