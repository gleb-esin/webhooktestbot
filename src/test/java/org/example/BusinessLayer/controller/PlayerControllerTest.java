package org.example.BusinessLayer.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.EntityLayer.Card;
import org.example.EntityLayer.Deck;
import org.example.EntityLayer.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
class PlayerControllerTest {
    Player attacker;
    Player defender;
    Player thrower1;
    Player thrower2;
    Player thrower3;
    Player thrower4;
    PlayerController playerController;

    @BeforeEach
    void setUp() {
        attacker = new Player(4L, "Attacker");
        defender = new Player(3L, "Defender");
        thrower1 = new Player(5L, "Thrower1");
        thrower2 = new Player(6L, "Thrower2");
        thrower3 = new Player(7L, "Thrower3");
        thrower4 = new Player(8L, "Thrower4");
        attacker.getPlayerHand().add(new Card("♠", "7", true));
        defender.getPlayerHand().add(new Card("♣", "7", false));
        thrower1.getPlayerHand().add(new Card("♦", "8", false));
        thrower2.getPlayerHand().add(new Card("♠", "9", false));
        thrower3.getPlayerHand().add(new Card("♣", "10", false));
        thrower4.getPlayerHand().add(new Card("♦", "J", false));
    }

    @Test
    void setPlayersTurn_whenCardIsTrumpAndWeightNotSet() {

        List<Player> players = new ArrayList<>(List.of(attacker, defender));
        playerController = new PlayerController(players);
        playerController.setPlayersTurn();

        assertEquals(101, attacker.getMinTrumpWeight());
    }

    @Test
    void setPlayersTurn_whenCardIsTrumpAndWeightIstSet_ButSetWeightIsLess() {
        attacker.getPlayerHand().add(new Card("♠", "6", true));

        List<Player> players = new ArrayList<>(List.of(attacker, defender));
        playerController = new PlayerController(players);
        playerController.setPlayersTurn();

        assertEquals(100, attacker.getMinTrumpWeight());
    }

    @Test
    void setPlayersTurn_whenCardIsTrumpAndWeightIstSet_ButSetWeightIsGreater() {
        attacker.getPlayerHand().add(new Card("♠", "8", true));

        List<Player> players = new ArrayList<>(List.of(attacker, defender));
        playerController = new PlayerController(players);
        playerController.setPlayersTurn();

        assertEquals(101, attacker.getMinTrumpWeight());
    }




    @Test
    void setPlayersTurn_whenCardIsNotTrumpAndWeightNotSet() {

        List<Player> players = new ArrayList<>(List.of(attacker, defender));
        playerController = new PlayerController(players);
        playerController.setPlayersTurn();

        assertEquals(1001, defender.getMinTrumpWeight());
    }

    @Test
    void setPlayersTurn_whenCardIsNotTrumpAndWeightIstSet_ButSetWeightIsLess() {
        defender.getPlayerHand().add(new Card("♣", "6", false));

        List<Player> players = new ArrayList<>(List.of(attacker, defender));
        playerController = new PlayerController(players);
        playerController.setPlayersTurn();

        assertEquals(1001, defender.getMinTrumpWeight());
    }

    @Test
    void setPlayersTurn_whenCardIsNotTrumpAndWeightIstSet_ButSetWeightIsGreater() {
        defender.getPlayerHand().add(new Card("♣", "8", false));

        List<Player> players = new ArrayList<>(List.of(attacker, defender));
        playerController = new PlayerController(players);
        playerController.setPlayersTurn();

        assertEquals(1001, defender.getMinTrumpWeight());
    }


    @Test
    void setPlayersTurn_when2Players() {
        List<Player> players = new ArrayList<>(List.of(attacker, defender));
        playerController = new PlayerController(players);
        playerController.setPlayersTurn();

        assertEquals("attacker", playerController.getAttacker().getRole());
        assertEquals("defender", playerController.getDefender().getRole());
    }

    @Test
    void setPlayersTurn_when3Players() {
        List<Player> players = new ArrayList<>(List.of(attacker, defender, thrower1));
        playerController = new PlayerController(players);
        playerController.setPlayersTurn();

        assertEquals("attacker", playerController.getAttacker().getRole());
        assertEquals("defender", playerController.getDefender().getRole());
        assertEquals("thrower", thrower1.getRole());
    }

    @Test
    void setPlayersTurn_when4Players() {
        List<Player> players = new ArrayList<>(List.of(attacker, defender, thrower1, thrower2));
        playerController = new PlayerController(players);
        playerController.setPlayersTurn();

        assertEquals("attacker", playerController.getAttacker().getRole());
        assertEquals("defender", playerController.getDefender().getRole());
        assertEquals("thrower", thrower1.getRole());
        assertEquals("thrower", thrower2.getRole());
    }

    @Test
    void setPlayersTurn_when5Players() {
        List<Player> players = new ArrayList<>(List.of(attacker, defender, thrower1, thrower2, thrower3));
        playerController = new PlayerController(players);
        playerController.setPlayersTurn();

        assertEquals("attacker", playerController.getAttacker().getRole());
        assertEquals("defender", playerController.getDefender().getRole());
        assertEquals("thrower", thrower1.getRole());
        assertEquals("thrower", thrower2.getRole());
        assertEquals("thrower", thrower3.getRole());
    }
    @Test
    void setPlayersTurn_when6Players() {
        List<Player> players = new ArrayList<>(List.of(attacker, defender, thrower1, thrower2, thrower3, thrower4));
        playerController = new PlayerController(players);
        playerController.setPlayersTurn();

        assertEquals("attacker", playerController.getAttacker().getRole());
        assertEquals("defender", playerController.getDefender().getRole());
        assertEquals("thrower", thrower1.getRole());
        assertEquals("thrower", thrower2.getRole());
        assertEquals("thrower", thrower3.getRole());
        assertEquals("thrower", thrower4.getRole());
    }

    @Test
    void changeTurn_when6Players_AndNoBinder() {
        List<Player> players = new ArrayList<>(List.of(attacker, defender, thrower1, thrower2, thrower3, thrower4));
        playerController = new PlayerController(players);
        playerController.setPlayersTurn();

        playerController.changeTurn();

        assertEquals("thrower", attacker.getRole());
        assertEquals("attacker", defender.getRole());
        assertEquals("defender", thrower1.getRole());
        assertEquals("thrower", thrower2.getRole());
        assertEquals("thrower", thrower3.getRole());
        assertEquals("thrower", thrower4.getRole());
    }

    @Test
    void changeTurn_withBinder() {
        List<Player> players = new ArrayList<>(List.of(attacker, defender, thrower1, thrower2, thrower3, thrower4));
        playerController = new PlayerController(players);
        playerController.setPlayersTurn();
        playerController.setBinder(playerController.getDefender());

        playerController.changeTurn();

        assertEquals("thrower", attacker.getRole());
        assertEquals("thrower", defender.getRole());
        assertEquals("attacker", thrower1.getRole());
        assertEquals("defender", thrower2.getRole());
        assertEquals("thrower", thrower3.getRole());
        assertEquals("thrower", thrower4.getRole());
    }

    @Test
    void isPlayerWinner_whenDeckAndPlayerHandAreEmpty_thenTrue() {
        attacker.getPlayerHand().clear();
        Deck deck = new Deck(UUID.randomUUID());
        deck.getDeck().clear();
        List<Player> players = new ArrayList<>(List.of(attacker, defender, thrower1, thrower2, thrower3, thrower4));
        playerController = new PlayerController(players);

        assertTrue(playerController.isPlayerWinner(attacker, deck));
    }

    @Test
    void isPlayerWinner_whenDeckIsNotEmptyAndPlayerHandIsEmpty_thenFalse() {
        attacker.getPlayerHand().clear();
        Deck deck = new Deck(UUID.randomUUID());
        List<Player> players = new ArrayList<>(List.of(attacker, defender, thrower1, thrower2, thrower3, thrower4));
        playerController = new PlayerController(players);

        assertFalse(playerController.isPlayerWinner(attacker, deck));
    }


    @Test
    void isPlayerWinner_whenDeckIsEmptyAndPlayerHandIsNot_thenFalse() {
        Deck deck = new Deck(UUID.randomUUID());
        deck.getDeck().clear();
        List<Player> players = new ArrayList<>(List.of(attacker, defender, thrower1, thrower2, thrower3, thrower4));
        playerController = new PlayerController(players);

        assertFalse(playerController.isPlayerWinner(attacker, deck));
    }

    @Test
    void isPlayerWinner_whenDeckAndPlayerHandAreNotEmpty_thenFalse() {
        Deck deck = new Deck(UUID.randomUUID());
        List<Player> players = new ArrayList<>(List.of(attacker, defender, thrower1, thrower2, thrower3, thrower4));
        playerController = new PlayerController(players);

        assertFalse(playerController.isPlayerWinner(attacker, deck));
    }
}