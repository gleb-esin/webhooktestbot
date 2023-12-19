package org.example.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.model.Card;
import org.example.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

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
        attacker.getPlayerHand().add(new Card("♠", "10", true));
        defender.getPlayerHand().add(new Card("♣", "6", false));
        thrower1.getPlayerHand().add(new Card("♦", "7", false));
        thrower2.getPlayerHand().add(new Card("♠", "8", false));
        thrower3.getPlayerHand().add(new Card("♣", "9", false));
        thrower4.getPlayerHand().add(new Card("♦", "10", false));
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
    void changeTurn_noBinder() {
        List<Player> players = new ArrayList<>(List.of(attacker, defender, thrower1, thrower2, thrower3, thrower4));
        playerController = new PlayerController(players);
        playerController.setPlayersTurn();
        playerController.changeTurn();

        assertEquals("defender", playerController.getAttacker().getRole());
        assertEquals("thrower", playerController.getDefender().getRole());
        assertEquals("attacker", thrower1.getRole());
        assertEquals("thrower", thrower2.getRole());
        assertEquals("thrower", thrower3.getRole());
        assertEquals("thrower", thrower4.getRole());
    }

    @Test
    void changeTurn_withBinder() {
        Player attacker = new Player(4L, "Attacker");
        Player defender = new Player(3L, "Defender");
        Player thrower = new Player(5L, "Thrower");
        attacker.setRole("attacker");
        defender.setRole("defender");
        List<Player> players = new ArrayList<>(List.of(attacker, defender, thrower));
        PlayerController playerController = new PlayerController(players);
//        playerController.setThrowQueue(new LinkedList<>(players));
        playerController.getThrowQueue().addFirst(thrower);
        playerController.getThrowQueue().addFirst(attacker);
        playerController.setAttacker(attacker);
        playerController.setDefender(defender);
        playerController.setBinder(defender);

        playerController.changeTurn();

        assertNull(defender.getRole());
        assertEquals("defender", attacker.getRole());
        assertEquals("attacker", thrower.getRole());
    }
}