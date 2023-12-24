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
import static org.mockito.Mockito.*;

class AttackTest {
    Player attacker;
    Player defender;
    TableController tableController;
    PlayerController playerController;
    ArrayList<Card> cards;
    @Mock
    TelegramBot bot;
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
        cards = new ArrayList<>(List.of(
                new Card("♠", "6", true),
                new Card("♣", "6", false)));

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

        verify(bot).sendMessageToAll(playerController.getPlayers(), expexceted.toString());
        verify(bot).sendMessageTo(attacker, attacker.toString());
    }

    @Test
    void move() {
        when(playerInputValidator.askForCards(any(), any())).thenReturn(cards);

        attack.move(playerController.getAttacker(), tableController, playerController);

        assertEquals(cards, tableController.getAll());
        verify(bot).sendMessageToAll(playerController.getPlayers(), tableController.getTable().toString());
        assertEquals("thrower", attacker.getRole());

    }
}