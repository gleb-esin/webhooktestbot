package org.example.move;

import org.example.controller.PlayerController;
import org.example.controller.TableController;
import org.example.model.Card;
import org.example.model.Player;
import org.example.model.Suit;
import org.example.network.TelegramBot;
import org.example.service.PlayerInputValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DefenceTest {
    Player attacker;
    Player defender;
    ArrayList<Card> defenderCardsCorrect;
    TableController tableController;
    PlayerController playerController;
    ArrayList<Card> cards;
    @Mock
    TelegramBot bot;
    @Mock
    PlayerInputValidator playerInputValidator;
    @InjectMocks
    Defence defence;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        attacker = new Player(1L, "attacker");
        defender = new Player(2L, "defender");
        tableController = new TableController(new Suit("♠", true));
        playerController = new PlayerController(List.of(attacker, defender));
        playerController.setAttacker(attacker);
        playerController.setDefender(defender);
        cards = new ArrayList<>(List.of(
                new Card("♠", "7", true),
                new Card("♣", "7", false)));
        tableController.addCardsToTable(cards, attacker);
        defenderCardsCorrect = new ArrayList<>(List.of(
                new Card("♠", "8", true),
                new Card("♣", "8", false)));
        defender.getPlayerHand().addAll(defenderCardsCorrect);

    }

    @Test
    void init() {
        defence.init(playerController);
        verify(bot).sendMessageToAll(playerController.getPlayers(),
                "\uD83D\uDEE1 Отбивается " + playerController.getDefender().getName() + " \uD83D\uDEE1");
        verify(bot).sendMessageTo(playerController.getDefender(), playerController.getDefender().toString());
    }

    @Test
    void move_whenDefenceIsCorrect_thenCadsAreBeaten() {
        when(playerInputValidator.askForCards(defender, bot)).thenReturn(defenderCardsCorrect);

        defence.move(defender, playerController.getPlayers(), tableController);

        assertEquals(4, tableController.getTable().getBeatenCards().size());
        verify(bot).sendMessageToAll(playerController.getPlayers(), tableController.getTable().toString());
        verify(bot).sendMessageToAll(playerController.getPlayers(), "Карты отбиты!");

    }

    @Test
    void move_whenDefenceIsNotCorrect_thenSendWarning() {
        ArrayList<Card> defenderCardsWrong = new ArrayList<>(List.of(
                new Card("♠", "6", true),
                new Card("♣", "6", false)));
        defender.getPlayerHand().addAll(defenderCardsWrong);
        when(playerInputValidator.askForCards(defender, bot)).thenReturn(defenderCardsWrong).thenReturn(defenderCardsCorrect);

        defence.move(defender, playerController.getPlayers(), tableController);

        verify(playerInputValidator, times(2)).askForCards(any(), any());
        verify(bot).sendMessageTo(defender, "Так не получится отбиться");
    }

    @Test
    void move_whenDefenceIsNotCorrectButThenDefenderRefuseToDefence_thenSetDefenderBinder() {
        ArrayList<Card> defenderCardsWrong = new ArrayList<>(List.of(
                new Card("♠", "6", true),
                new Card("♣", "6", false)));
        defender.getPlayerHand().addAll(defenderCardsWrong);
        when(playerInputValidator.askForCards(defender, bot)).thenReturn(defenderCardsWrong).thenReturn(List.of());

        defence.move(defender, playerController.getPlayers(), tableController);

        assertEquals("binder", defender.getRole());
        assertEquals(2, tableController.getAll().size());
    }

    @Test
    void move_whenDefenceIsNotPossible_thenSetDefenderBinder() {
        ArrayList<Card> defenderCardsWrong = new ArrayList<>(List.of(
                new Card("♠", "6", true),
                new Card("♣", "6", false)));
        defender.getPlayerHand().clear();
        defender.getPlayerHand().addAll(defenderCardsWrong);
        when(playerInputValidator.askForCards(defender, bot)).thenReturn(defenderCardsWrong).thenReturn(defenderCardsCorrect);

        defence.move(defender, playerController.getPlayers(), tableController);

        assertEquals("binder", defender.getRole());
        assertEquals(2, tableController.getAll().size());
    }

    @Test
    void move_whenDefenderDoNotWantDefence_thenTableIsEmpty () {
        ArrayList<Card> defenderCardsEmpty = new ArrayList<>();
        defender.getPlayerHand().addAll(defenderCardsEmpty);
        when(playerInputValidator.askForCards(defender, bot)).thenReturn(defenderCardsEmpty);

        defence.move(defender, playerController.getPlayers(), tableController);

        verify(bot).sendMessageToAll(playerController.getPlayers(), defender.getName() + " не будет отбиваться");
        assertEquals("binder", defender.getRole());
        assertEquals(2, tableController.getAll().size());
    }
}