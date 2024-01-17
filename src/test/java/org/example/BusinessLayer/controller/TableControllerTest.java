package org.example.BusinessLayer.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.EntityLayer.Card;
import org.example.EntityLayer.Deck;
import org.example.EntityLayer.Player;
import org.example.EntityLayer.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@FieldDefaults(level = AccessLevel.PRIVATE)
class TableControllerTest {
    Deck deck;
    List<Card> playerCards;
    Player player;
    TableController tableController;

    @BeforeEach
    void setUp() {
        deck = new Deck();
        playerCards = new ArrayList<>();
        player = new Player(new Random().nextLong(), "Player");
        tableController = new TableController(new Table());
        tableController.setTrump(deck.getTrump());
        for (int i = 0; i < 6; i++) {
            player.getPlayerHand().add(deck.getNextCard());
        }
        for (int i = 0; i < 3; i++) {
            playerCards.add(player.getPlayerHand().get(i));
        }
        for (int i = 0; i < 3; i++) {
            tableController.getTable().getUnbeatenCards().add(deck.getNextCard());
        }
    }

    @Test
    void addCardsToTable_whenRoleIsDefender_thenUnbeatenCardsIsEmpty_AndBeatenCardsContains6Cards() {
        player.setRole("defender");

        tableController.addCardsToTable(playerCards, player);

        assertEquals(3, player.getPlayerHand().size());
        assertEquals(6, tableController.getTable().getBeatenCards().size());
        assertEquals(0, tableController.getTable().getUnbeatenCards().size());
    }

    @Test
    void addCardsToTable_whenRoleIsAttacker_thenUnbeatenCardsContains6_AndBeatenCardsIsEmpty() {
        player.setRole("attacker");

        tableController.addCardsToTable(playerCards, player);

        assertEquals(3, player.getPlayerHand().size());
        assertEquals(0, tableController.getTable().getBeatenCards().size());
        assertEquals(6, tableController.getTable().getUnbeatenCards().size());
    }

    @Test
    void addCardsToTable_whenRoleIsThrower_thenUnbeatenCardsContains6_AndBeatenCardsIsEmpty() {
        player.setRole("thrower");

        tableController.addCardsToTable(playerCards, player);

        assertEquals(3, player.getPlayerHand().size());
        assertEquals(0, tableController.getTable().getBeatenCards().size());
        assertEquals(6, tableController.getTable().getUnbeatenCards().size());
    }

    @Test
    void clear() {
        tableController.clear();

        assertEquals(0, tableController.getAll().size());
    }

    @Test
    void getAll() {
        assertEquals(3, tableController.getAll().size());
    }

    @Test
    void getTable() {

        assertNotNull(tableController.getTable());
    }
}