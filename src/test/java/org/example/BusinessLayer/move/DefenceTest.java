package org.example.BusinessLayer.move;

import org.example.BusinessLayer.controller.PlayerController;
import org.example.BusinessLayer.controller.TableController;
import org.example.EntityLayer.Card;
import org.example.EntityLayer.Player;
import org.example.EntityLayer.Suit;
import org.example.ServiseLayer.services.MessageService;
import org.example.ServiseLayer.services.PlayerInputValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DefenceTest {
    AutoCloseable closeable;
    Player attacker;
    Player defender;
    ArrayList<Card> defenderCardsCorrect;
    TableController tableController;
    PlayerController playerController;
    ArrayList<Card> cards;
    @Mock
    MessageService messageService;
    @Mock
    PlayerInputValidator playerInputValidator;
    @InjectMocks
    Defence defence;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
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

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void init() {
        defence.init(playerController);
        verify(messageService).sendMessageToAll(playerController.getPlayers(),
                "\uD83D\uDEE1 Отбивается " + playerController.getDefender().getName() + " \uD83D\uDEE1");
        verify(messageService).sendMessageTo(playerController.getDefender(), playerController.getDefender().toString());
    }

    @Test
    void move_whenDefenceIsCorrect_thenCadsAreBeaten() {
        when(playerInputValidator.askForCards(defender)).thenReturn(defenderCardsCorrect);

        defence.move(defender, playerController.getPlayers(), tableController);

        assertEquals(4, tableController.getTable().getBeatenCards().size());
        verify(messageService).sendMessageToAll(playerController.getPlayers(), tableController.getTable().toString());
        verify(messageService).sendMessageToAll(playerController.getPlayers(), "Карты отбиты!");

    }

    @Test
    void move_whenDefenceIsNotCorrect_thenSendWarning() {
        ArrayList<Card> defenderCardsWrong = new ArrayList<>(List.of(
                new Card("♠", "6", true),
                new Card("♣", "6", false)));
        defender.getPlayerHand().addAll(defenderCardsWrong);
        when(playerInputValidator.askForCards(defender)).thenReturn(defenderCardsWrong).thenReturn(defenderCardsCorrect);

        defence.move(defender, playerController.getPlayers(), tableController);

        verify(playerInputValidator, times(2)).askForCards(any());
        verify(messageService).sendMessageTo(defender, "Так не получится отбиться");
    }

    @Test
    void move_whenDefenceIsNotCorrectButThenDefenderRefuseToDefence_thenSetDefenderBinder() {
        ArrayList<Card> defenderCardsWrong = new ArrayList<>(List.of(
                new Card("♠", "6", true),
                new Card("♣", "6", false)));
        defender.getPlayerHand().addAll(defenderCardsWrong);
        when(playerInputValidator.askForCards(defender)).thenReturn(defenderCardsWrong).thenReturn(List.of());

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
        when(playerInputValidator.askForCards(defender)).thenReturn(defenderCardsWrong).thenReturn(defenderCardsCorrect);

        defence.move(defender, playerController.getPlayers(), tableController);

        assertEquals("binder", defender.getRole());
        assertEquals(2, tableController.getAll().size());
    }

    @Test
    void move_whenDefenderDoNotWantDefence_thenTableIsEmpty () {
        ArrayList<Card> defenderCardsEmpty = new ArrayList<>();
        defender.getPlayerHand().addAll(defenderCardsEmpty);
        when(playerInputValidator.askForCards(defender)).thenReturn(defenderCardsEmpty);

        defence.move(defender, playerController.getPlayers(), tableController);

        verify(messageService).sendMessageToAll(playerController.getPlayers(), defender.getName() + " не будет отбиваться");
        assertEquals("binder", defender.getRole());
        assertEquals(2, tableController.getAll().size());
    }
}