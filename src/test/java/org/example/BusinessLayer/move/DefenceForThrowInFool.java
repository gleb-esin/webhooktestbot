package org.example.BusinessLayer.move;

import org.example.BusinessLayer.controller.PlayerController;
import org.example.BusinessLayer.controller.TableController;
import org.example.EntityLayer.Card;
import org.example.EntityLayer.Player;
import org.example.EntityLayer.Suit;
import org.example.EntityLayer.Table;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DefenceForThrowInFoolTest {
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
    DefenceForThrowInFool defence;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        attacker = new Player(1L, "attacker");
        defender = new Player(2L, "defender");
        tableController = new TableController(new Table());
        tableController.setTrump(new Suit("♠", true));
        playerController = new PlayerController();
        playerController.setPlayers(List.of(attacker, defender));
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
    void move_whenDefenceIsCorrect_thenCadsAreBeaten() {
        when(playerInputValidator.askForCards(defender)).thenReturn(defenderCardsCorrect);

        defence.move(playerController, tableController);

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

        defence.move(playerController, tableController);

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

        defence.move(playerController, tableController);

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

        defence.move(playerController, tableController);

        assertEquals("binder", defender.getRole());
        assertEquals(2, tableController.getAll().size());
    }

    @Test
    void move_whenDefenderDoNotWantDefence_thenTableIsEmpty () {
        ArrayList<Card> defenderCardsEmpty = new ArrayList<>();
        defender.getPlayerHand().addAll(defenderCardsEmpty);
        when(playerInputValidator.askForCards(defender)).thenReturn(defenderCardsEmpty);

        defence.move(playerController, tableController);

        verify(messageService).sendMessageToAll(playerController.getPlayers(), defender.getName() + " не будет отбиваться");
        assertEquals("binder", defender.getRole());
        assertEquals(2, tableController.getAll().size());
    }
}