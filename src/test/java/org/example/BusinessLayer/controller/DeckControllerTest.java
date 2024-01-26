package org.example.BusinessLayer.controller;

import org.example.EntityLayer.Deck;
import org.example.EntityLayer.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Deque;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class DeckControllerTest {
    DeckController deckController;
    Deck deck;
    Player attacker;
    Player defender;
    Deque<Player> throwQueue;

    @BeforeEach
    public void setUp() {
        deck = new Deck();
        deckController = spy(new DeckController(deck));
        attacker = new Player(4L, "Attacker");
        defender = new Player(3L, "Defender");
        throwQueue = new LinkedList<>();
        throwQueue.add(attacker);
    }

    @Test
    void fillUpThePlayersHand_whenPlayerHasNotEnoughCards_thenFilledUpTo6cardsInHand() {
        deckController.fillUpThePlayersHand(attacker);
        assertEquals(6, attacker.getPlayerHand().size());

    }

    @Test
    void fillUpThePlayersHand_whenPlayerHas6Cards_thenHandDoesNotFilledUp() {
        for (int i = 0; i < 6; i++) {
            attacker.getPlayerHand().add(deck.getNextCard());
        }

        deckController.fillUpThePlayersHand(attacker);

        assertEquals(6, attacker.getPlayerHand().size());
    }

    @Test
    void fillUpThePlayersHand_whenPlayerHasMoreThan6Cards_thenHandDoesNotFilledUp() {
        for (int i = 0; i < 8; i++) {
            attacker.getPlayerHand().add(deck.getNextCard());
        }

        deckController.fillUpThePlayersHand(attacker);

        assertEquals(8, attacker.getPlayerHand().size());
    }

    @Test
    void fillUpTheHands_whenDeckHasMoreCardsThanNeededForPlayers_thenFillUpThePlayersHandIsInvoke() {
        deckController.fillUpTheHands(throwQueue, defender);

        verify(deckController, times(2)).fillUpThePlayersHand(any());

    }

    @Test
    void fillUpTheHands_whenDeckHasLessCardsThanNeededForPlayers_thenFillUpThePlayersHandIsNotInvoke() {
        for (int i = 0; i < 25; i++) {
            deck.getNextCard();
        }
        deckController.fillUpTheHands(throwQueue, defender);

        verify(deckController, never()).fillUpThePlayersHand(any());
    }

    @Test
    void fillUpTheHands_whenDeckHasLessCardsThanNeededForPlayers_thenDeckIsEmpty() {
        for (int i = 0; i < 25; i++) {
            deck.getNextCard();
        }

        deckController.fillUpTheHands(throwQueue, defender);

        assertTrue(deck.isEmpty());
    }

    @Test
    void fillUpTheHands_whenDeckHasMoreCardsThanNeededForPlayers_thenDeckIsDecreases() {

        deckController.fillUpTheHands(throwQueue, defender);

        assertEquals(24, deck.getDeckSize());
    }

    @Test
    void illUpTheHands_whenDeckHasMoreCardsThanNeededForPlayers_thenDeckIsDecreases () {
        for (int i = 0; i < 24; i++) {
            deck.getNextCard();
        }

        deckController.fillUpTheHands(throwQueue, defender);

        assertEquals(0, deck.getDeckSize());
    }
}