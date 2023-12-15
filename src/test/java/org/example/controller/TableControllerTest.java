package org.example.controller;

import org.example.model.Card;
import org.example.model.Deck;
import org.example.model.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TableControllerTest {

    @Test
    void addCardsToTable_defender() {
        Deck deck = new Deck(UUID.randomUUID());
        List<Card> playerCards = new ArrayList<>();
        Player player = new Player(new Random().nextLong(), "Player");
        player.setRole("defender");
        for (int i = 0; i < 6; i++) {
            player.getPlayerHand().add(deck.getNextCard());
        }
        for (int i = 0; i < 3; i++) {
            playerCards.add(player.getPlayerHand().get(i));
        }
        TableController tableController = new TableController(null);
        for (int i = 0; i < 3; i++) {
            tableController.getTable().getUnbeatenCards().add(deck.getNextCard());
        }

        tableController.addCardsToTable(playerCards, player);

        assertEquals(3, player.getPlayerHand().size());
        assertEquals(6, tableController.getTable().getBeatenCards().size());
        assertEquals(0, tableController.getTable().getUnbeatenCards().size());
    }

    @Test
    void addCardsToTable_attackerOrThrower() {
        Deck deck = new Deck(UUID.randomUUID());
        List<Card> playerCards = new ArrayList<>();
        Player player = new Player(new Random().nextLong(), "Player");
        player.setRole("attacker");
        for (int i = 0; i < 6; i++) {
            player.getPlayerHand().add(deck.getNextCard());
        }
        for (int i = 0; i < 3; i++) {
            playerCards.add(player.getPlayerHand().get(i));
        }

        TableController tableController = new TableController(null);
        for (int i = 0; i < 3; i++) {
            tableController.getTable().getUnbeatenCards().add(deck.getNextCard());
        }

        tableController.addCardsToTable(playerCards, player);

        assertEquals(3, player.getPlayerHand().size());
        assertEquals(0, tableController.getTable().getBeatenCards().size());
        assertEquals(6, tableController.getTable().getUnbeatenCards().size());
    }
}