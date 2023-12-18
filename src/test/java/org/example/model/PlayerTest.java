package org.example.model;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void toStringTest() {
        Player player = new Player(new Random().nextLong(), "Player");
        player.getPlayerHand().add(new Card("♠", "7", true));
        player.getPlayerHand().add(new Card("♠", "10", true));
        player.getPlayerHand().add(new Card("♥", "A", false));
        player.getPlayerHand().add(new Card("♦", "K", false));
        player.getPlayerHand().add(new Card("♠", "J", true));
        player.getPlayerHand().add(new Card("♠", "Q", true));
        player.getPlayerHand().add(new Card("♠", "9", true));

        String expected = "Player\n" +
                "<b>[K♦]</b><b>[A♥]</b><b>[7♠]</b><b>[9♠]</b><b>[10♠]</b><b>[ J♠]</b>\n" +
                "   1         2        3        4           5        6        \n" +
                "<b>[Q♠]</b>\n" +
                "   7         ";

        assertEquals(expected, player.toString());

    }

    @Test
    void constructor_UserEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(1L);
        userEntity.setName("Player");
        userEntity.setWins(10);
        userEntity.setGames(100);

        Player player = new Player(userEntity);

        assertEquals("<b>Player</b>", player.getName());
        assertEquals(10, player.getWins());
        assertEquals(100, player.getGames());
        assertEquals(1L, player.getChatID());
    }


    @Test
    void compareTo() {
        Player player1 = new Player(new Random().nextLong(), "Player1");
        player1.setMinTrumpWeight(105);
        Player player2 = new Player(new Random().nextLong(), "Player2");
        player2.setMinTrumpWeight(1005);

        assertTrue(player1.compareTo(player2) < 0);
        assertTrue(player2.compareTo(player1) > 0);
    }

    @Test
    void toUserEntity() {
        Player player = new Player(1L, "Player");
        player.setWins(10);
        player.setGames(100);

        UserEntity userEntity = player.toUserEntity();

        assertEquals(1L, userEntity.getUserId());
        assertEquals("Player", userEntity.getName());
        assertEquals(10, userEntity.getWins());
        assertEquals(100, userEntity.getGames());
    }

    @Test
    void getStatistics_when1() {
        Player player = new Player(1L, "Player");
        player.setWins(1);
        player.setGames(1);

        String expected = "У вас 1 победа и 1 игра";

        assertEquals(expected, player.getStatistics());
    }

    @Test
    void getStatistics_when2() {
        Player player = new Player(1L, "Player");
        player.setWins(2);
        player.setGames(2);
        String expected = "У вас 2 победы и 2 игры";

        assertEquals(expected, player.getStatistics());
    }

    @Test
    void getStatistics_when5() {
        int n = 5;
        Player player = new Player(1L, "Player");
        player.setWins(n);
        player.setGames(n);
        String expected = "У вас " + n + " побед и " + n + " игр";

        assertEquals(expected, player.getStatistics());
    }

    @Test
    void getStatistics_when12() {
        int n = 12;
        Player player = new Player(1L, "Player");
        player.setWins(n);
        player.setGames(n);
        String expected = "У вас " + n + " побед и " + n + " игр";

        assertEquals(expected, player.getStatistics());
    }

    @Test
    void getStatistics_when122() {
        int n = 122;
        Player player = new Player(1L, "Player");
        player.setWins(n);
        player.setGames(n);
        String expected = "У вас " + n + " победы и " + n + " игры";

        assertEquals(expected, player.getStatistics());
    }

}