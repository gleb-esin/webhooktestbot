package org.example.service;

import org.example.model.Deck;
import org.example.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PlayerInputValidatorTest implements PlayerInputValidator {
    Player player;
    Deck deck;

    @BeforeEach
    void setUp() {
        player = new Player(1L, "Player");
        deck = new Deck(UUID.randomUUID());
        for (int i = 0; i < 5; i++) {
            player.getPlayerHand().add(deck.getNextCard());
        }
    }


    @Test
    void parseCardIndexesStringToPlayerHandIndexes_whenInputIsCorrect() {
        String cardIndexes = "1 2 3 4";
        List<Integer> playerHandIndexes = parseCardIndexesStringToPlayerHandIndexes(cardIndexes);
        assertEquals(List.of(1, 2, 3, 4), playerHandIndexes);
    }

    @Test
    void parseCardIndexesStringToPlayerHandIndexes_whenInputContainsZero() {
        String cardIndexes = "1 2 3 0";
        List<Integer> playerHandIndexes = parseCardIndexesStringToPlayerHandIndexes(cardIndexes);
        assertEquals(List.of(), playerHandIndexes);
    }

    @Test
    void parseCardIndexesStringToPlayerHandIndexes_whenInputContainsRepeatedIndexes() {
        String cardIndexes = "1 2 3 4 2 3 4";
        List<Integer> playerHandIndexes = parseCardIndexesStringToPlayerHandIndexes(cardIndexes);
        assertEquals(List.of(1, 2, 3, 4), playerHandIndexes);
    }

    @Test
    void parseCardIndexesStringToPlayerHandIndexes_whenInputIsNotCorrect() {
        String cardIndexes = "1 2 3 4, b #!";
        List<Integer> playerHandIndexes = parseCardIndexesStringToPlayerHandIndexes(cardIndexes);
        assertEquals(List.of(1, 2, 3), playerHandIndexes);
    }

    @Test
    void validatePlayerHandIndexes_whenListIsCorrect() {
        List<Integer> playerHandIndexes = List.of(1, 2, 3, 4);
        boolean correctInput = validatePlayerHandIndexes(playerHandIndexes, player);
        assertTrue(correctInput);
    }

    @Test
    void validatePlayerHandIndexes_whenListIsEmpty() {
        List<Integer> playerHandIndexes = List.of();
        boolean correctInput = validatePlayerHandIndexes(playerHandIndexes, player);
        assertTrue(correctInput);
    }

    @Test
    void validatePlayerHandIndexes_whenListIsLargerThanPlayersHand() {
        List<Integer> playerHandIndexes = List.of(1,2,3,4,5,6,7);
        boolean correctInput = validatePlayerHandIndexes(playerHandIndexes, player);
        assertFalse(correctInput);
    }

    @Test
    void validatePlayerHandIndexes_whenListContainsIndexThatIsLargerThanPlayersHand() {
        List<Integer> playerHandIndexes = List.of(1,2,7);
        boolean correctInput = validatePlayerHandIndexes(playerHandIndexes, player);
        assertFalse(correctInput);
    }
}