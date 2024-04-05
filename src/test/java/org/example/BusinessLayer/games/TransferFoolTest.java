package org.example.BusinessLayer.games;

import org.example.BusinessLayer.controller.DeckController;
import org.example.BusinessLayer.controller.PlayerController;
import org.example.BusinessLayer.controller.TableController;
import org.example.BusinessLayer.move.Attack;
import org.example.BusinessLayer.move.Defence;
import org.example.BusinessLayer.move.DefenceForTransferFool;
import org.example.BusinessLayer.move.Throw;
import org.example.EntityLayer.Card;
import org.example.EntityLayer.Player;
import org.example.EntityLayer.Table;
import org.example.ServiseLayer.services.MessageService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransferFoolTest {
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
    DefenceForTransferFool defence;
    @Mock
    Throw throwMove;
    @Spy
    @InjectMocks
    TransferFool transferFool;
    @Mock
    List<Player> players;
    @Mock
    Player attacker;
    @Mock
    Player thrower;
    @Mock
    Player defender;
    @Mock
    Table table;
    LinkedList<Player> throwQueue;

    @BeforeEach
    void setUp() {
        closeable = org.mockito.MockitoAnnotations.openMocks(this);
        players = List.of(attacker, thrower);
        doNothing().when(transferFool).init(any());
        doNothing().when(transferFool).sendMessageWithGameWinner(any());
        doNothing().when(transferFool).binderGrabsCards(any());
        doNothing().when(transferFool).clearTable();
        doNothing().when(transferFool).fillUpPlayerHands(any());
        doNothing().when(transferFool).changeTurn();

        when(tableController.getTable()).thenReturn(table);
        when(table.getUnbeatenCards()).thenReturn(mock(List.class));
        throwQueue = new LinkedList<>(players);
        when(playerController.getDefender()).thenReturn(defender);
        when(defender.getRole()).thenReturn("defender");
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void play_whenGameIsOver_thenSendMessageWithGameWinner() {
        when(transferFool.gameIsNotOver()).thenReturn(false);

        transferFool.play(players);

        verify(transferFool).sendMessageWithGameWinner(any());
    }

    @Test
    void play_whenGameIsNotOver_thenCallAttack() {
        when(transferFool.gameIsNotOver())
                .thenReturn(true);
        when(transferFool.attack()).thenReturn(true);

        transferFool.play(players);

        verify(transferFool).attack();
    }

    @Test
    void play_whenAttackerBecomesWinner_thenSendMessageWithGameWinner() {
        when(transferFool.gameIsNotOver())
                .thenReturn(true);
        when(transferFool.attack()).thenReturn(true);

        transferFool.play(players);

        verify(transferFool).sendMessageWithGameWinner(players);
    }

    @Test
    void play_whenAttackerNotBecomesWinner_thenDefenceIsCalled() {
        when(transferFool.gameIsNotOver())
                .thenReturn(true);
        when(transferFool.attack()).thenReturn(false);
        when(transferFool.defence()).thenReturn(true);

        transferFool.play(players);

        verify(transferFool).defence();
    }

    @Test
    void play_whenDefenderBecomesWinner_thenSendMessageWithGameWinner() {
        when(transferFool.gameIsNotOver())
                .thenReturn(true);
        when(transferFool.attack()).thenReturn(false);
        when(transferFool.defence()).thenReturn(true);

        transferFool.play(players);

        verify(transferFool).sendMessageWithGameWinner(players);
    }


    @Test
    void play_whenDefenceIsTransfer_thenChangeDefenderIsCalled() {
        doNothing().when(transferFool).changeDefender();
        when(transferFool.gameIsNotOver())
                .thenReturn(true);
        when(transferFool.attack()).thenReturn(false);
        when(transferFool.defence())
                .thenReturn(false)
                .thenReturn(true);
        when(transferFool.isDefenceIsTransfer()).thenReturn(true);

        transferFool.play(players);

        verify(transferFool).changeDefender();
    }

    @Test
    void play_whenDefenceIsNotTransfer_thenThrowMoveIsCalled() {
        when(transferFool.gameIsNotOver())
                .thenReturn(true);
        when(transferFool.attack()).thenReturn(false);
        when(transferFool.defence())
                .thenReturn(false);
        when(transferFool.isDefenceIsTransfer()).thenReturn(false);
        when(transferFool.throwMove()).thenReturn(true);

        transferFool.play(players);

        verify(transferFool).throwMove();
    }

    @Test
    void play_whenThrowerBecomesWinner_thenSendMessageWithGameWinner() {
        when(transferFool.gameIsNotOver())
                .thenReturn(true);
        when(transferFool.attack()).thenReturn(false);
        when(transferFool.defence())
                .thenReturn(false);
        when(transferFool.isDefenceIsTransfer()).thenReturn(false);
        when(transferFool.throwMove()).thenReturn(true);

        transferFool.play(players);

        verify(transferFool).sendMessageWithGameWinner(any());
    }

    @Test
    void play_whenThrowerNotBecomesWinner_thenBinderGrabsCardsIsCalled() {
        when(transferFool.gameIsNotOver())
                .thenReturn(true)
                .thenReturn(false);
        when(transferFool.attack()).thenReturn(false);
        when(transferFool.defence())
                .thenReturn(false);
        when(transferFool.isDefenceIsTransfer()).thenReturn(false);
        when(transferFool.throwMove()).thenReturn(false);

        transferFool.play(players);

        verify(transferFool).binderGrabsCards(players);
    }

    @Test
    void play_whenBinderGrabsCardsHasCalled_thenClearTableIsCalled() {
        when(transferFool.gameIsNotOver())
                .thenReturn(true)
                .thenReturn(false);
        when(transferFool.attack()).thenReturn(false);
        when(transferFool.defence())
                .thenReturn(false);
        when(transferFool.isDefenceIsTransfer()).thenReturn(false);
        when(transferFool.throwMove()).thenReturn(false);

        transferFool.play(players);

        verify(transferFool).clearTable();
    }

    @Test
    void play_whenClearTableHasCalled_thenFillUpPlayerHandsIsCalled() {
        when(transferFool.gameIsNotOver())
                .thenReturn(true)
                .thenReturn(false);
        when(transferFool.attack()).thenReturn(false);
        when(transferFool.defence())
                .thenReturn(false);
        when(transferFool.isDefenceIsTransfer()).thenReturn(false);
        when(transferFool.throwMove()).thenReturn(false);

        transferFool.play(players);

        verify(transferFool).fillUpPlayerHands(players);
    }

    @Test
    void play_whenFillUpPlayerHandsHasCalled_thenChangeTurnIsCalled() {
        when(transferFool.gameIsNotOver())
                .thenReturn(true)
                .thenReturn(false);
        when(transferFool.attack()).thenReturn(false);
        when(transferFool.defence())
                .thenReturn(false);
        when(transferFool.isDefenceIsTransfer()).thenReturn(false);
        when(transferFool.throwMove()).thenReturn(false);

        transferFool.play(players);

        verify(transferFool).changeTurn();
    }

    @Test
    void changeDefender_whenThrowQueueSizeIsEqualOne_thenNextDefenderIsAttacker() {
        throwQueue.remove(1);
        when(playerController.getThrowQueue()).thenReturn(throwQueue);

        transferFool.changeDefender();

        verify(playerController).setDefender(attacker);
    }

    @Test
    void changeDefender_whenThrowQueueSizeIsMoreThanOne_thenNextDefenderIsThrower() {
        when(playerController.getThrowQueue()).thenReturn(throwQueue);

        transferFool.changeDefender();

        verify(playerController).setDefender(thrower);
    }

    @Test
    void isDefenceIsTransfer_whenValuesAreEquals_thenReturnTrue() {
        List<Card> valueIsEquals = List.of(new Card("♠", "6", false), new Card("♣", "6", false));
        Table table = mock(Table.class);
        when(tableController.getTable()).thenReturn(table);
        when(table.getUnbeatenCards()).thenReturn(valueIsEquals);

        boolean expected = transferFool.isDefenceIsTransfer();

        assertTrue(expected);
    }

    @Test
    void isDefenceIsTransfer_whenValuesAreNotEquals_thenReturnFalse() {
        List<Card> valueIsNotEquals = List.of(new Card("♠", "6", false), new Card("♣", "6", false),  new Card("♣", "7", false));
        Table table = mock(Table.class);
        when(tableController.getTable()).thenReturn(table);
        when(table.getUnbeatenCards()).thenReturn(valueIsNotEquals);

        boolean expected = transferFool.isDefenceIsTransfer();

        assertFalse(expected);
    }
}