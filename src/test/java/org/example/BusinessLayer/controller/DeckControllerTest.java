package org.example.BusinessLayer.controller;

import org.example.EntityLayer.Deck;
import org.example.EntityLayer.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Deque;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeckControllerTest {
    DeckController deckController;
    @BeforeEach
    public void setUp() {
        deckController = new DeckController(new Deck());
    }

    @Test
    void test_fillUpThePlayersHand() {
        Player player = new Player(1L, null);
        deckController.fillUpThePlayersHand(player);
        player.getPlayerHand().remove(0);
        player.getPlayerHand().remove(0);

        deckController.fillUpThePlayersHand(player);

        assertEquals(6, player.getPlayerHand().size());
    }

    @Test
    void test_fillUpTheHands_fullDeck() {
        Player attacker = new Player(4L, "Attacker");
        Player defender = new Player(3L, "Defender");
        Player thrower = new Player(5L, "Thrower");
        Deque<Player> throwQueue = new LinkedList<>();
        throwQueue.add(attacker);
        throwQueue.add(thrower);
        throwQueue.add(defender);
        throwQueue.forEach(player -> deckController.fillUpThePlayersHand(player));
        attacker.getPlayerHand().remove(0);
        attacker.getPlayerHand().remove(0);
        thrower.getPlayerHand().remove(0);
        defender.getPlayerHand().remove(0);
        defender.getPlayerHand().remove(0);
        defender.getPlayerHand().remove(0);
        throwQueue.remove(defender);

        deckController.fillUpTheHands(throwQueue, defender);

        assertEquals(6, attacker.getPlayerHand().size());
        assertEquals(6, thrower.getPlayerHand().size());
        assertEquals(6, defender.getPlayerHand().size());
    }

    @Test
    void test_fillUpTheHands_almostEmptyDeck() {
        //create test environment with:
        //deck size = 4
        //defender hand size = 3
        //thrower hand size = 5
        //attacker hand size = 4
        Player attacker = new Player(4L, "Attacker");
        Player defender = new Player(3L, "Defender");
        Player thrower = new Player(5L, "Thrower");
        Deque<Player> throwQueue = new LinkedList<>();
        throwQueue.add(attacker);
        throwQueue.add(thrower);
        throwQueue.add(defender);
        throwQueue.forEach(player -> deckController.fillUpThePlayersHand(player));
        attacker.getPlayerHand().remove(0);
        attacker.getPlayerHand().remove(0);
        thrower.getPlayerHand().remove(0);
        defender.getPlayerHand().remove(0);
        defender.getPlayerHand().remove(0);
        defender.getPlayerHand().remove(0);
        throwQueue.remove(defender);
        throwQueue.remove(defender);
        deckController.getDeck().getDeck().subList(0, 14).clear();
        DeckController deckControllerSpy = Mockito.spy(deckController);
        deckControllerSpy.fillUpTheHands(throwQueue, defender);

        Mockito.verify(deckControllerSpy).fillUpTheHandsForTheLastTime(throwQueue, defender);
    }

    @Test
    void test_fillUpTheHandsForTheLastTime() {
        //create test environment with:
        //deck size = 4
        //defender hand size = 3
        //thrower hand size = 5
        //attacker hand size = 4
        Player attacker = new Player(4L, "Attacker");
        Player defender = new Player(3L, "Defender");
        Player thrower = new Player(5L, "Thrower");
        Deque<Player> throwQueue = new LinkedList<>();
        throwQueue.add(attacker);
        throwQueue.add(thrower);
        throwQueue.add(defender);
        throwQueue.forEach(player -> deckController.fillUpThePlayersHand(player));
        attacker.getPlayerHand().remove(0);
        attacker.getPlayerHand().remove(0);
        thrower.getPlayerHand().remove(0);
        defender.getPlayerHand().remove(0);
        defender.getPlayerHand().remove(0);
        defender.getPlayerHand().remove(0);
        throwQueue.remove(defender);
        deckController.getDeck().getDeck().subList(0, 14).clear();

        deckController.fillUpTheHandsForTheLastTime(throwQueue, defender);

        assertEquals(6, attacker.getPlayerHand().size());
        assertEquals(6, thrower.getPlayerHand().size());
        assertEquals(4, defender.getPlayerHand().size());
    }
}