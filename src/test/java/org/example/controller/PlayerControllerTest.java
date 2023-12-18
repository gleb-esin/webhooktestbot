package org.example.controller;

import org.example.model.Card;
import org.example.model.Player;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PlayerControllerTest {

    @Test
    void setPlayersTurn() {
        Player attacker = new Player(4L, "Attacker");
        Player defender = new Player(3L, "Defender");
        Player thrower = new Player(5L, "Thrower");
        attacker.getPlayerHand().add(new Card("♠", "10", true));
        defender.getPlayerHand().add(new Card("♣", "10", false));
        thrower.getPlayerHand().add(new Card("♦", "A", false));
        thrower.getName();

        PlayerController playerController = new PlayerController(new ArrayList<>(List.of(attacker, defender, thrower)));
        playerController.setPlayersTurn();

        assertEquals(playerController.getAttacker(), attacker);
        assertEquals(playerController.getDefender(), defender);
    }

    @Test
    void changeTurn_noBinder() {
        Player attacker = new Player(4L, "Attacker");
        Player defender = new Player(3L, "Defender");
        Player thrower = new Player(5L, "Thrower");
        attacker.setRole("attacker");
        defender.setRole("defender");
        List<Player> players = new ArrayList<>(List.of(attacker, defender, thrower));
        PlayerController playerController = new PlayerController(players);
        playerController.setThrowQueue(new LinkedList<>(players));
        playerController.getThrowQueue().addFirst(thrower);
        playerController.getThrowQueue().addFirst(attacker);
        playerController.setAttacker(attacker);
        playerController.setDefender(defender);

        playerController.changeTurn();

        assertNull(attacker.getRole());
        assertEquals("defender", thrower.getRole());
        assertEquals("attacker", defender.getRole());
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
        playerController.setThrowQueue(new LinkedList<>(players));
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