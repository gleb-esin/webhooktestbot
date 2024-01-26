package org.example.EntityLayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {
    Deck deck;

    @BeforeEach
    void setUp() {
        deck = new Deck();

    }


    @Test
    void testToString() {
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
        String expected = "<b>[6♠]</b> <b>[7♠]</b> <b>[8♠]</b> <b>[9♠]</b> <b>[10♠]</b> <b>[ J♠]</b> <b>[Q♠]</b> <b>[K♠]</b> <b>[A♠]</b> <b>[6♣]</b> <b>[7♣]</b> <b>[8♣]</b> <b>[9♣]</b> <b>[10♣]</b> <b>[ J♣]</b> <b>[Q♣]</b> <b>[K♣]</b> <b>[A♣]</b> <b>[6♥]</b> <b>[7♥]</b> <b>[8♥]</b> <b>[9♥]</b> <b>[10♥]</b> <b>[ J♥]</b> <b>[Q♥]</b> <b>[K♥]</b> <b>[A♥]</b> <b>[6♦]</b> <b>[7♦]</b> <b>[8♦]</b> <b>[9♦]</b> <b>[10♦]</b> <b>[ J♦]</b> <b>[Q♦]</b> <b>[K♦]</b> <b>[A♦]</b> ";

        assertEquals(expected, deck.toString());
    }

    @Test
    void getNextCard_whenCalled_thenGetNextCard() {
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
        Card expected = new Card("♠", "6", false);

        Card actual = deck.getNextCard();

        assertEquals(expected, actual);
        assertFalse(deck.getDeck().contains(actual));
    }

    @Test
    void getNextCard_whenCalled_thenDeckSizeIsDecreased() {
        deck.getNextCard();
        int actual = deck.getDeckSize();

        assertEquals(35, actual);
    }

    @Test
    void isEmpty() {
        deck.getDeck().clear();

        assertTrue(deck.isEmpty());
    }

    @Test
    void Deck_whenCreated_thenContains36cards() {
        deck = new Deck();

        assertEquals(36, deck.getDeckSize());
    }

    @Test
    void Deck_whenCreated_thenDoesNotContainsEqualCards() {
        int coincedence = 0;
        deck = new Deck();
        for(Card card : deck.getDeck()) {
            for(Card card2 : deck.getDeck()) {
                if(card.equals(card2)) {
                    coincedence++;

                }
            }
        }
            assertEquals(36, coincedence);
    }
}