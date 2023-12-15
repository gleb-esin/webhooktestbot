package org.example.model;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void compareTo() {
        Player player1 = new Player(new Random().nextLong(), "Player1");
        player1.setMinTrumpWeight(105);
        Player player2 = new Player(new Random().nextLong(), "Player2");
        player2.setMinTrumpWeight(1005);

        assertTrue(player1.compareTo(player2) < 0);
    }
}