package org.example.EntityLayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {
    Deck deck;

    @BeforeEach
    void setUp() {
        deck = new Deck();
        deck.getDeck().clear();
        String[] suitArr = {"♠", "♣", "♥", "♦"};
        String trumpStr = "♣";
        boolean isTrump;
        String[] valuesArr = {"6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        for (String suit : suitArr) {
            isTrump = trumpStr.equals(suit);
            for (String value : valuesArr) {
                deck.getDeck().add(new Card(suit, value, isTrump));
            }
        }
    }


    @Test
    void testToString() {
        String expected = "<b>[6♠]</b> <b>[7♠]</b> <b>[8♠]</b> <b>[9♠]</b> <b>[10♠]</b> <b>[ J♠]</b> <b>[Q♠]</b> <b>[K♠]</b> <b>[A♠]</b> <b>[6♣]</b> <b>[7♣]</b> <b>[8♣]</b> <b>[9♣]</b> <b>[10♣]</b> <b>[ J♣]</b> <b>[Q♣]</b> <b>[K♣]</b> <b>[A♣]</b> <b>[6♥]</b> <b>[7♥]</b> <b>[8♥]</b> <b>[9♥]</b> <b>[10♥]</b> <b>[ J♥]</b> <b>[Q♥]</b> <b>[K♥]</b> <b>[A♥]</b> <b>[6♦]</b> <b>[7♦]</b> <b>[8♦]</b> <b>[9♦]</b> <b>[10♦]</b> <b>[ J♦]</b> <b>[Q♦]</b> <b>[K♦]</b> <b>[A♦]</b> ";

        assertEquals(expected, deck.toString());
    }

    @Test
    void getNextCard() {
        Card actual = deck.getNextCard();
        Card expected = new Card("♠", "6", false);
        assertEquals(expected, actual);
        assertFalse(deck.getDeck().contains(actual));
    }

    @Test
    void isEmpty() {
        deck.getDeck().clear();

        assertTrue(deck.isEmpty());
    }
}