package org.example.BusinessLayer.games;

import org.example.BusinessLayer.controller.DeckController;
import org.example.BusinessLayer.controller.PlayerController;
import org.example.BusinessLayer.controller.TableController;
import org.example.BusinessLayer.move.*;
import org.example.EntityLayer.Deck;
import org.example.EntityLayer.Player;
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
import static org.mockito.Mockito.*;

public class AbstractFoolTest {
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
    DefenceForThrowInFool defence;
    @Mock
    Throw throwMove;
    @Spy
    @InjectMocks
    AbstractFoolImpl abstractFoolImpl;
    @Mock
    Player player;
    @Mock
    Deck deck;

    @BeforeEach
    void setUp() {
        closeable = org.mockito.MockitoAnnotations.openMocks(this);
        player = new Player(1L, "player");
        when(playerController.getWinner()).thenReturn(player);
        doNothing().when(playerController).changeTurn();
        doNothing().when(tableController).clear();
        when(playerController.isGameOver()).thenReturn(false);
        when(deckController.getDeck()).thenReturn(deck);
        doNothing().when(deckController).fillUpTheHands(any(), any());
        when(playerController.getDefender()).thenReturn(player);
        when(playerController.getThrowQueue()).thenReturn(new LinkedList<>(List.of(player)));

    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void sendMessageWithGameWinner() {
        List<Player> players = List.of(player);
        abstractFoolImpl.sendMessageWithGameWinner(players);

        verify(messageService, times(1)).sendMessageToAll(players, "\uD83C\uDFC6 Победил " + player.getName() + "! \uD83C\uDFC6");
    }

    @Test
    void changeTurn() {
        abstractFoolImpl.changeTurn();

        verify(playerController, times(1)).changeTurn();

    }

    @Test
    void clearTable() {
        abstractFoolImpl.clearTable();

        verify(tableController, times(1)).clear();
    }

    @Test
    void gameIsNotOver() {
        boolean isTrue = abstractFoolImpl.gameIsNotOver();

        verify(playerController, times(1)).isGameOver();
        assertTrue(isTrue);
    }

    @Test
    void fillUpPlayerHands_whenDeckIsNotEmpty_thenDeckControllerFilledUpTheHandsIsCalled() {
        when(deck.isEmpty()).thenReturn(false);

        abstractFoolImpl.fillUpPlayerHands(List.of(player));

        verify(deckController, times(1)).fillUpTheHands(any(), any());
    }

    @Test
    void fillUpPlayerHands_whenDeckIsEmptyAndDeckIsEmptyMessageNotSent_thenSendMessageToIsCalled_andFillUpPlayerHandsIsNotCalled() {
        when(deck.isEmpty()).thenReturn(true);

        abstractFoolImpl.fillUpPlayerHands(List.of(player));

        verify(messageService).sendMessageToAll(List.of(player), "\uD83D\uDE45 <b>Колода пуста!</b> \uD83D\uDE45");
    }

    @Test
    void binderGrabsCards_whenDefenderIsBinder_thenPlayerControllerSetBinderIsCalled() {
        player.setRole("binder");
        when(playerController.getBinder()).thenReturn(player);

        abstractFoolImpl.binderGrabsCards(List.of(player));

        verify(playerController).setBinder(player);
        verify(messageService).sendMessageToAll(List.of(player), playerController.getBinder().getName() + " забирает карты " + tableController.getAll().toString().substring(1, tableController.getAll().toString().length() - 1));
    }

    @Test
    void binderGrabsCards_whenDefenderIsNotBinder_thenPlayerControllerSetBinderIsNotCalled() {
        player.setRole("defender");
        when(playerController.getBinder()).thenReturn(player);

        abstractFoolImpl.binderGrabsCards(List.of(player));

        verify(playerController, times(0)).setBinder(player);
    }

    @Test
    void binderGrabsCards_whenDefenderIsBinder_thenMessageServiceSendMessageToAllIsCalled() {
        player.setRole("binder");
        when(playerController.getBinder()).thenReturn(player);

        abstractFoolImpl.binderGrabsCards(List.of(player));

        verify(messageService).sendMessageToAll(List.of(player), playerController.getBinder().getName() + " забирает карты " + tableController.getAll().toString().substring(1, tableController.getAll().toString().length() - 1));
    }

    @Test
    void binderGrabsCards_whenDefenderIsNotBinder_thenMessageServiceSendMessageToAllIsNotCalled() {
        player.setRole("defender");
        when(playerController.getBinder()).thenReturn(player);

        abstractFoolImpl.binderGrabsCards(List.of(player));

        verify(messageService, times(0)).sendMessageToAll(List.of(player), playerController.getBinder().getName() + " забирает карты " + tableController.getAll().toString().substring(1, tableController.getAll().toString().length() - 1));
    }

    @Test
    void throwMove_whenThrowIsPossible_thenReturnFalse() {
        when(throwMove.isThrowPossible(tableController.getAll(), player, playerController.getDefender()))
                .thenReturn(true)
                .thenReturn(false);

        boolean expected = abstractFoolImpl.throwMove();

        assertFalse(expected);
    }

    @Test
    void throwMove_whenThrowerIsBecomeAWinner_thenReturnTrue() {
        when(throwMove.isThrowPossible(tableController.getAll(), player, playerController.getDefender())).thenReturn(true);
        when(playerController.isPlayerWinner(player, deckController.getDeck())).thenReturn(true);

        boolean expected = abstractFoolImpl.throwMove();

        assertTrue(expected);
    }

    @Test
    void throwMove_whenDefenceIsNeeded_thenDefenceInitIsCalled() {
        when(throwMove.isThrowPossible(tableController.getAll(), player, playerController.getDefender()))
                .thenReturn(true)
                .thenReturn(false);
        when(playerController.isPlayerWinner(player, deckController.getDeck())).thenReturn(false);
        when(throwMove.move(any(), any(), any(), any())).thenReturn(true);

        abstractFoolImpl.throwMove();

        verify(defence).init(any());
    }

    @Test
    void throwMove_whenDefenceIsNeeded_thenDefenceMoveIsCalled() {
        when(throwMove.isThrowPossible(tableController.getAll(), player, playerController.getDefender()))
                .thenReturn(true)
                .thenReturn(false);
        when(playerController.isPlayerWinner(player, deckController.getDeck())).thenReturn(false);
        when(throwMove.move(any(), any(), any(), any())).thenReturn(true);

        abstractFoolImpl.throwMove();

        verify(defence).move(any(), any());
    }

    @Test
    void throwMove_whenDefenderIsWinner_thenReturnTrue() {
        when(throwMove.isThrowPossible(tableController.getAll(), player, playerController.getDefender()))
                .thenReturn(true);
        when(playerController.isPlayerWinner(player, deckController.getDeck()))
                .thenReturn(false)
                .thenReturn(true);
        when(throwMove.move(any(), any(), any(), any())).thenReturn(true);

        boolean expected = abstractFoolImpl.throwMove();

        assertTrue(expected);
    }


    @Test
    void defence_whenInvoked_thenDefenceInitIsCalled() {
        abstractFoolImpl.defence();

        verify(defence).init(any());
    }

    @Test
    void defence_whenInvoked_thenDefenceMoveIsCalled() {
        abstractFoolImpl.defence();

        verify(defence).move(any(), any());
    }

    @Test
    void defence_whenDefenderBecomesWinner_thenReturnTrue() {
        when(playerController.isPlayerWinner(any(), any())).thenReturn(true);

        boolean expected = abstractFoolImpl.defence();

        assertTrue(expected);
    }

    @Test
    void defence_whenDefenderNotBecomesWinner_thenReturnFalse() {
        when(playerController.isPlayerWinner(any(), any())).thenReturn(false);

        boolean expected = abstractFoolImpl.defence();

        assertFalse(expected);
    }

    @Test
    void attack_whenInvoked_thenAttackInitIsCalled() {
        abstractFoolImpl.attack();

        verify(attack).init(any(), any());
    }

    @Test
    void attack_whenInvoked_thenAttackMoveIsCalled() {
        abstractFoolImpl.attack();

        verify(attack).move(any(), any());
    }

    @Test
    void attack_whenDefenderBecomesWinner_thenReturnTrue() {
        when(playerController.isPlayerWinner(any(), any())).thenReturn(true);

        boolean expected = abstractFoolImpl.attack();

        assertTrue(expected);
    }

    @Test
    void attack_whenDefenderNotBecomesWinner_thenReturnFalse() {
        when(playerController.isPlayerWinner(any(), any())).thenReturn(false);

        boolean expected = abstractFoolImpl.attack();

        assertFalse(expected);
    }

    @Test
    void init_whenInvoked_thenFillUpThePlayersHandIsCalled() {
        abstractFoolImpl.init(List.of(player));

        verify(deckController).fillUpThePlayersHand(player);
    }

    @Test
    void init_whenInvoked_thenPlayerControllerSetPlayersIsCalled() {
        abstractFoolImpl.init(List.of(player));

        verify(playerController).setPlayers(List.of(player));
    }

    @Test
    void init_whenInvoked_thenPlayerControllerSetPlayersTurnIsCalled() {
        abstractFoolImpl.init(List.of(player));

        verify(playerController).setPlayersTurn();
    }

    @Test
    void init_whenInvoked_thenTableControllerSetTrumpIsCalled() {
        abstractFoolImpl.init(List.of(player));

        verify(tableController).setTrump(deckController.getTrump());
    }
}

class AbstractFoolImpl extends AbstractFool {

    protected AbstractFoolImpl(PlayerController playerController, DeckController deckController, TableController tableController, MessageService messageService, Attack attack, AbstractDefence defence, Throw throwMove) {
        super(playerController, deckController, tableController, messageService, attack, defence, throwMove);
    }

    @Override
    public void play(List<Player> players) {

    }
}