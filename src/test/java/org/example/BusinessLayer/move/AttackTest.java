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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AttackTest {
    AutoCloseable closeable;
    Player attacker;
    Player defender;
    TableController tableController;
    PlayerController playerController;
    ArrayList<Card> cards;
    @Mock
    MessageService messageService;
    @Mock
    PlayerInputValidator playerInputValidator;
    @InjectMocks
    Attack attack;

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
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void init() {
        String expexceted = "⚔️ Ход " +
                attacker.getName() +
                " под " +
                defender.getName() +
                "⚔️" +
                System.lineSeparator() +
                tableController.getTable().toString();

        attack.init(playerController, tableController);

        verify(messageService).sendMessageToAll(playerController.getPlayers(), expexceted);
        verify(messageService).sendMessageTo(attacker, attacker.toString());
    }

    @Test
    void move_whenMoveIsCorrect_thenAddCardsToTable() {
        cards = new ArrayList<>(List.of(
                new Card("♠", "6", true),
                new Card("♣", "6", false)));
        when(playerInputValidator.askForCards(any())).thenReturn(cards);

        attack.move(playerController, tableController);

        assertEquals(cards, tableController.getAll());
        verify(messageService).sendMessageToAll(playerController.getPlayers(), tableController.getTable().toString());
        assertEquals("thrower", attacker.getRole());

    }

    @Test
    void move_whenAttackIsNotCorrect_thenSendWarning() {
        ArrayList<Card> wrongCards = new ArrayList<>(List.of(
                new Card("♠", "6", true),
                new Card("♣", "7", false)));
        cards = new ArrayList<>(List.of(
                new Card("♠", "6", true),
                new Card("♣", "6", false)));
        when(playerInputValidator.askForCards(any())).
                thenReturn(wrongCards).thenReturn(cards);

        attack.move(playerController, tableController);
        verify(messageService).sendMessageTo(attacker, "Так пойти не получится.");
        verify(playerInputValidator, times(2)).askForCards(any());
    }
}