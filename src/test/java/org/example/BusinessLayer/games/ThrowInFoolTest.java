package org.example.BusinessLayer.games;

import org.example.BusinessLayer.controller.DeckController;
import org.example.BusinessLayer.controller.PlayerController;
import org.example.BusinessLayer.controller.TableController;
import org.example.BusinessLayer.move.Attack;
import org.example.BusinessLayer.move.Defence;
import org.example.BusinessLayer.move.DefenceFotThrowInFool;
import org.example.BusinessLayer.move.Throw;
import org.example.EntityLayer.Player;
import org.example.ServiseLayer.services.MessageService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ThrowInFoolTest {
    AutoCloseable closeable;
    @Mock
    PlayerController playerController;
    @Mock
    DeckController deckController;
    @Mock
    TableController tableController;
    @Mock
    MessageService messageService;
    @Mock
    Attack attack;
    @Mock
    DefenceFotThrowInFool defence;
    @Mock
    Throw throwMove;
    @Spy
    @InjectMocks
    ThrowInFool throwInFool;
    List<Player> players;


    @BeforeEach
    void setUp() {
        closeable = org.mockito.MockitoAnnotations.openMocks(this);
        players = List.of(mock(Player.class), mock(Player.class));
        doNothing().when(throwInFool).sendMessageWithGameWinner(any());
        doNothing().when(throwInFool).binderGrabsCards(any());
        doNothing().when(throwInFool).clearTable();
        doNothing().when(throwInFool).fillUpPlayerHands(any());
        doNothing().when(throwInFool).changeTurn();
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void play_whenGameIsOver_thenSendMessageWithGameWinner() {
        when(throwInFool.gameIsNotOver()).thenReturn(false);

        throwInFool.play(players);

        verify(throwInFool).sendMessageWithGameWinner(any());
    }

    @Test
    void play_whenGameIsNotOver_thenCallAttack() {
        when(throwInFool.gameIsNotOver())
                .thenReturn(true);
        when(throwInFool.attack()).thenReturn(true);

        throwInFool.play(players);

        verify(throwInFool).attack();
    }
    @Test
    void play_whenAttackerBecomesWinner_thenSendMessageWithGameWinner() {
        when(throwInFool.gameIsNotOver())
                .thenReturn(true);
        when(throwInFool.attack()).thenReturn(true);

        throwInFool.play(players);

        verify(throwInFool).sendMessageWithGameWinner(players);
    }

    @Test
    void play_whenAttackerNotBecomesWinner_thenDefenceIsCalled() {
        when(throwInFool.gameIsNotOver())
                .thenReturn(true);
        when(throwInFool.attack()).thenReturn(false);
        when(throwInFool.defence()).thenReturn(true);

        throwInFool.play(players);

        verify(throwInFool).defence();
    }

    @Test
    void play_whenDefenderBecomesWinner_thenSendMessageWithGameWinner() {
        when(throwInFool.gameIsNotOver())
                .thenReturn(true);
        when(throwInFool.attack()).thenReturn(false);
        when(throwInFool.defence()).thenReturn(true);

        throwInFool.play(players);

        verify(throwInFool).sendMessageWithGameWinner(players);
    }

    @Test
    void play_whenDefenderNotBecomesWinner_thenThrowMoveIsCalled() {
        when(throwInFool.gameIsNotOver())
                .thenReturn(true);
        when(throwInFool.attack()).thenReturn(false);
        when(throwInFool.defence()).thenReturn(false);
        when(throwInFool.throwMove()).thenReturn(true);

        throwInFool.play(players);

        verify(throwInFool).throwMove();
    }

    @Test
    void play_whenThrowerBecomesWinner_thenSendMessageWithGameWinner() {
        when(throwInFool.gameIsNotOver())
                .thenReturn(true);
        when(throwInFool.attack()).thenReturn(false);
        when(throwInFool.defence()).thenReturn(false);
        when(throwInFool.throwMove()).thenReturn(true);

        throwInFool.play(players);

        verify(throwInFool).sendMessageWithGameWinner(players);
    }

    @Test
    void play_whenThrowerNotBecomesWinner_thenBinderGrabsCardsIsCalled() {
        when(throwInFool.gameIsNotOver())
                .thenReturn(true)
                .thenReturn(false);
        when(throwInFool.attack()).thenReturn(false);
        when(throwInFool.defence()).thenReturn(false);
        when(throwInFool.throwMove()).thenReturn(false);

        throwInFool.play(players);

        verify(throwInFool).binderGrabsCards(players);
    }

    @Test
    void play_whenBinderGrabsCardsHasCalled_thenClearTableIsCalled() {
        when(throwInFool.gameIsNotOver())
                .thenReturn(true)
                .thenReturn(false);
        when(throwInFool.attack()).thenReturn(false);
        when(throwInFool.defence()).thenReturn(false);
        when(throwInFool.throwMove()).thenReturn(false);

        throwInFool.play(players);

        verify(throwInFool).clearTable();
    }

    @Test
    void play_whenClearTableHasCalled_thenFillUpPlayerHandsIsCalled() {
        when(throwInFool.gameIsNotOver())
                .thenReturn(true)
                .thenReturn(false);
        when(throwInFool.attack()).thenReturn(false);
        when(throwInFool.defence()).thenReturn(false);
        when(throwInFool.throwMove()).thenReturn(false);

        throwInFool.play(players);

        verify(throwInFool).fillUpPlayerHands(players);
    }

    @Test
    void play_whenFillUpPlayerHandsHasCalled_thenChangeTurnIsCalled() {
        when(throwInFool.gameIsNotOver())
                .thenReturn(true)
                .thenReturn(false);
        when(throwInFool.attack()).thenReturn(false);
        when(throwInFool.defence()).thenReturn(false);
        when(throwInFool.throwMove()).thenReturn(false);

        throwInFool.play(players);

        verify(throwInFool).changeTurn();
    }
}