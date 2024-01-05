package org.example.move;

import org.example.usecases.controller.PlayerController;
import org.example.usecases.controller.TableController;
import org.example.entities.Card;
import org.example.entities.Player;
import org.example.entities.Suit;
import org.example.usecases.move.Attack;
import org.example.interfaceAdapters.service.MessageService;
import org.example.interfaceAdapters.service.PlayerInputValidator;
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
        MockitoAnnotations.openMocks(this);

        attacker = new Player(1L, "attacker");
        defender = new Player(2L, "defender");
        tableController = new TableController(new Suit("♠", true));
        playerController = new PlayerController(List.of(attacker, defender));
        playerController.setAttacker(attacker);
        playerController.setDefender(defender);
    }

    @Test
    void init() {
        StringBuilder expexceted = new StringBuilder("⚔️ Ход ")
                .append(attacker.getName())
                .append(" под ")
                .append(defender.getName())
                .append("⚔️")
                .append(System.lineSeparator())
                .append(tableController.getTable().toString());

        attack.init(playerController, tableController);

        verify(messageService).sendMessageToAll(playerController.getPlayers(), expexceted.toString());
        verify(messageService).sendMessageTo(attacker, attacker.toString());
    }

    @Test
    void move_whenMoveIsCorrect_thenAddCardsToTable() {
        cards = new ArrayList<>(List.of(
                new Card("♠", "6", true),
                new Card("♣", "6", false)));
        when(playerInputValidator.askForCards(any())).thenReturn(cards);

        attack.move(playerController.getAttacker(), tableController, playerController);

        assertEquals(cards, tableController.getAll());
//FIXME:
        //        verify(bot).sendMessageToAll(playerController.getPlayers(), tableController.getTable().toString());
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

        attack.move(playerController.getAttacker(), tableController, playerController);
////FIXME:
//        verify(bot).sendMessageTo(attacker, "Так пойти не получится.");
        verify(playerInputValidator, times(2)).askForCards(any());
    }
}