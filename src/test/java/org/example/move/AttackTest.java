package org.example.move;

import org.example.controller.PlayerController;
import org.example.controller.TableController;
import org.example.model.Card;
import org.example.model.Player;
import org.example.model.Table;
import org.example.network.TelegramBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AttackTest {
    PlayerController playerController;
    TableController tableController;
    Player attacker;
    Player defender;
    @Mock
    TelegramBot bot;
    Attack attack;

    @BeforeEach
    void setUp() {
        playerController = mock(PlayerController.class);
        tableController = mock(TableController.class);
        attacker = new Player(1L, "Attacker");

        attacker.getPlayerHand().add(new Card("♠", "6",true));
        attacker.getPlayerHand().add(new Card("♠", "7",true));
        attacker.getPlayerHand().add(new Card("♣", "6",true));
        attacker.getPlayerHand().add(new Card("♣", "9",true));
        attacker.getPlayerHand().add(new Card("♥", "6",true));
        attacker.getPlayerHand().add(new Card("♦", "6",true));

        defender = new Player(2L, "Defender");

        when(playerController.getAttacker()).thenReturn(attacker);
        when(playerController.getDefender()).thenReturn(defender);
        when(playerController.getPlayers()).thenReturn(new ArrayList<>(List.of(attacker, defender)));
        when(tableController.getTable()).thenReturn(mock(Table.class));
        bot = mock(TelegramBot.class);
        attack = new Attack(bot);


    }

    @Test
    void init() {
        StringBuilder expectedMessage = new StringBuilder("⚔️ Ход ")
                .append(attacker.getName())
                .append(" под ")
                .append(defender.getName())
                .append("⚔️")
                .append(System.lineSeparator())
                .append(tableController.getTable().toString());
        Attack spy = spy(attack);
        spy.init(playerController, tableController);

        verify(spy).sendMessageToAll(
                Mockito.eq(new ArrayList<>(List.of(attacker, defender))
                ), Mockito.eq(expectedMessage.toString()), Mockito.eq(bot));
    }

    @Test
    void move() {

        assertEquals("thrower", attacker.getRole());
    }
}