package org.example.entities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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

        String expected = """
                Player
                <b>[K♦]</b><b>[A♥]</b><b>[7♠]</b><b>[9♠]</b><b>[10♠]</b><b>[ J♠]</b>
                   1         2        3        4           5        6       \s
                <b>[Q♠]</b>
                   7        \s""";

        assertEquals(expected, player.toString());

    }

    @Test
    void constructor_UserEntity() {
        UserEntity userEntity = new UserEntity(1L, "Player");
        Player player = new Player(userEntity);

        assertEquals("<b>Player</b>", player.getName());
        assertEquals(0, player.getWins());
        assertEquals(0, player.getGames());
        assertEquals(1L, player.getChatID());
    }


    @ParameterizedTest
    @CsvSource({"5, 3, 1", "3, 5, -1", "4, 4, 0"})
    void compareTo(int weight1, int weight2, int expected) {
        Player player1 = new Player(1L, "Player1");
        player1.setMinTrumpWeight(weight1);

        Player player2 = new Player(2L, "Player2");
        player2.setMinTrumpWeight(weight2);

        int result = player1.compareTo(player2);

        assertEquals(expected, result);
    }

    @Test
    void toUserEntity() {
        Player player = new Player(1L, "Player");

        UserEntity userEntity = player.toUserEntity();

        assertEquals(1L, userEntity.getUserId());
        assertEquals("<b>Player</b>", userEntity.getName());
        assertEquals(0, userEntity.getWins());
        assertEquals(0, userEntity.getGames());
    }

    @ParameterizedTest
    @CsvSource({"1, 1, 'У вас 1 победа и 1 игра'",
            "2, 2, 'У вас 2 победы и 2 игры'",
            "5, 5, 'У вас 5 побед и 5 игр'",
            "12, 12, 'У вас 12 побед и 12 игр'",
            "122, 122, 'У вас 122 победы и 122 игры'"})
    void getStatistics(int wins, int games, String expected) {
        Player player = new Player(1L, "Player");
        player.setWins(wins);
        player.setGames(games);

        assertEquals(expected, player.getStatistics());
    }

}