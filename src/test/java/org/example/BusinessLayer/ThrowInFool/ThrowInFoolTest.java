package org.example.BusinessLayer.ThrowInFool;

import org.example.BusinessLayer.controller.DeckController;
import org.example.BusinessLayer.controller.PlayerController;
import org.example.BusinessLayer.controller.TableController;
import org.example.BusinessLayer.move.Attack;
import org.example.BusinessLayer.move.Defence;
import org.example.BusinessLayer.move.Throw;
import org.example.BusinessLayer.throwInFool.ThrowInFool;
import org.example.EntityLayer.Card;
import org.example.EntityLayer.Deck;
import org.example.EntityLayer.Player;
import org.example.ServiseLayer.services.MessageService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ThrowInFoolTest {
    AutoCloseable closeable;
    @Mock
    PlayerController playerController;
    @Mock
    Deck deck;
    @Mock
    DeckController deckController;
    @Mock
    TableController tableController;
    @Mock
    MessageService messageService;
    @Mock
    Attack attack;
    @Mock
    Defence defence;
    @Mock
    Throw throwMove;
    @InjectMocks
    ThrowInFool throwInFool;
    Player attacker;
    Player defender;
    Player thrower;
    List<Player> players;

    @BeforeEach
    void setUp() {
        closeable = org.mockito.MockitoAnnotations.openMocks(this);
        attacker = new Player(1L, "attacker");
        attacker.setRole("attacker");
        defender = new Player(2L, "defender");
        defender.setRole("defender");
        thrower = new Player(3L, "thrower");
        thrower.setRole("thrower");
        players = List.of(attacker, defender, thrower);
        when(playerController.getPlayers()).thenReturn(players);
        when(playerController.getAttacker()).thenReturn(attacker);
        when(playerController.getDefender()).thenReturn(defender);
        when(playerController.getThrowQueue()).thenReturn(new LinkedList<>(List.of(attacker, thrower)));
        when(deckController.getDeck()).thenReturn(deck);
        when(playerController.getWinner()).thenReturn(attacker);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void play_whenInvoked_thenDeckControllerFilledUpPlayersHand() {
        when(playerController.isGameOver()).thenReturn(true);
        throwInFool.play(players);

        verify(deckController, times(3)).fillUpThePlayersHand(any());
    }

    @Test
    void play_whenInvoked_thenPlayerControllerSetPlayers() {
        when(playerController.isGameOver()).thenReturn(true);
        throwInFool.play(players);

        verify(playerController, times(1)).setPlayers(players);
    }

    @Test
    void play_whenInvoked_thenPlayerControllerSetPlayerSetPlayersTurn() {
        when(playerController.isGameOver()).thenReturn(true);
        throwInFool.play(players);

        verify(playerController, times(1)).setPlayersTurn();
    }

    @Test
    void play_whenInvoked_thenTableControllerSetPlayerSetTrump() {
        when(playerController.isGameOver()).thenReturn(true);
        throwInFool.play(players);

        verify(tableController, times(1)).setTrump(deckController.getTrump());
    }

    @Test
    void throwInFoolTest_whenPlayerControllerGameOverIsTrue_thenSendFinalMessage() {
        when(playerController.isGameOver()).thenReturn(true);
        when(playerController.getWinner()).thenReturn(attacker);
        String finalMessage = "\uD83C\uDFC6 Победил <b>attacker</b>! \uD83C\uDFC6";

        throwInFool.play(players);

        verify(messageService, times(1)).sendMessageToAll(players, finalMessage);
    }

    @Test
    void throwInFoolTest_whenGameIsNotOver_thenSendAttackInitIsInvoked() {
        when(playerController.isGameOver()).thenReturn(false);
        when(playerController.isPlayerWinner(playerController.getAttacker(), deckController.getDeck())).thenReturn(true);
        when(playerController.getWinner()).thenReturn(attacker);

        throwInFool.play(players);

        when(playerController.isPlayerWinner(playerController.getDefender(), deckController.getDeck())).thenReturn(true);
        when(playerController.getWinner()).thenReturn(defender);
    }

    @Test
    void throwInFoolTest_whenAttackerBecomesWinner_thenSendFinalMessage() {
        when(playerController.isPlayerWinner(playerController.getAttacker(), deckController.getDeck())).thenReturn(true);
        when(playerController.getWinner()).thenReturn(attacker);
        String finalMessage = "\uD83C\uDFC6 Победил <b>attacker</b>! \uD83C\uDFC6";

        throwInFool.play(players);

        verify(messageService, times(1)).sendMessageToAll(players, finalMessage);
    }

    @Test
    void throwInFoolTest_whenAttackerNotBecomesWinner_thenDefenseInitIsInvoked() {
        when(playerController.isPlayerWinner(playerController.getDefender(), deckController.getDeck())).thenReturn(true);
        when(playerController.getWinner()).thenReturn(defender);

        throwInFool.play(players);

        verify(defence, times(1)).init(any());
    }

    @Test
    void throwInFoolTest_whenDefenderBecomesWinner_thenSendFinalMessage() {
        when(playerController.isPlayerWinner(playerController.getDefender(), deckController.getDeck())).thenReturn(true);
        when(playerController.getWinner()).thenReturn(defender);
        String finalMessage = "\uD83C\uDFC6 Победил <b>defender</b>! \uD83C\uDFC6";

        throwInFool.play(players);

        verify(messageService, times(1)).sendMessageToAll(players, finalMessage);
    }

    @Test
    void throwInFoolTest_whenDefenderNotBecomesWinner_thenIsThrowPossibleIsInvoked() {
        when(playerController.isPlayerWinner(thrower, deckController.getDeck())).thenReturn(true);
        when(throwMove.isThrowPossible(tableController.getAll(), thrower, playerController.getDefender())).thenReturn(true, false);
        when(playerController.getWinner()).thenReturn(thrower);

        throwInFool.play(players);

        verify(throwMove, times(1)).isThrowPossible(tableController.getAll(), thrower, playerController.getDefender());
    }

    @Test
    void throwInFoolTest_whenThrowerBecomesWinner_thenSendFinalMessage() {
        when(playerController.isPlayerWinner(thrower, deckController.getDeck())).thenReturn(true);
        when(throwMove.isThrowPossible(tableController.getAll(), thrower, playerController.getDefender())).thenReturn(true, false);
        when(playerController.getWinner()).thenReturn(thrower);
        String finalMessage = "\uD83C\uDFC6 Победил <b>thrower</b>! \uD83C\uDFC6";

        throwInFool.play(players);

        verify(messageService, times(1)).sendMessageToAll(players, finalMessage);
    }

    @Test
    void throwInFoolTest_whenThrowerNotBecomesWinnerAndAfterThrowMoveDefenceIsNeeded_thenDefenceInitIsInvokedTwicePerRound() {
        when(playerController.isGameOver()).thenReturn(false, true);
        when(throwMove.isThrowPossible(tableController.getAll(), thrower, playerController.getDefender())).thenReturn(true, false);
        when(throwMove.move(any(), any(), any(), any())).thenReturn(true);

        throwInFool.play(players);

        verify(defence, times(2)).init(any());
    }

    @Test
    void throwInFoolTest_whenThrowerNotBecomesWinnerButAfterThrowMoveDefenceIsNotNeeded_thenDefenceInitIsInvokedOncePerRound() {
        when(playerController.isGameOver()).thenReturn(false, true);
        when(throwMove.isThrowPossible(tableController.getAll(), thrower, playerController.getDefender())).thenReturn(true, false);
        when(throwMove.move(any(), any(), any(), any())).thenReturn(false);

        throwInFool.play(players);

        verify(defence, times(1)).init(any());
    }

    @Test
    void throwInFoolTest_whenDefenderIsBinder_thenSendMessageToBinder() {
        defender.setRole("binder");
        when(playerController.getBinder()).thenReturn(defender);
        when(playerController.isGameOver()).thenReturn(false, true);
        Card tableCard = new Card("♥", "6", false);
        when(tableController.getAll()).thenReturn(List.of(tableCard));
        String messageToBinder = playerController.getBinder().getName() + " забирает карты " + tableController.getAll().toString().substring(1, tableController.getAll().toString().length() - 1);

        throwInFool.play(players);

        verify(messageService).sendMessageToAll(players, messageToBinder);
    }

    @Test
    void throwInFoolTest_whenDefenderIsBinder_thenCardsFromTableMoveToDefenderHand() {
        defender.setRole("binder");
        when(playerController.getBinder()).thenReturn(defender);
        when(playerController.isGameOver()).thenReturn(false, true);
        Card tableCard = new Card("♥", "6", false);
        when(tableController.getAll()).thenReturn(List.of(tableCard));

        throwInFool.play(players);

        assertTrue(defender.getPlayerHand().contains(tableCard));
    }

    @Test
    void throwInFoolTest_whenDefenderIsBinder_thenPlayerControllerSetDefenderAsABinder() {
        defender.setRole("binder");
        when(playerController.getBinder()).thenReturn(defender);
        when(playerController.isGameOver()).thenReturn(false, true);
        Card tableCard = new Card("♥", "6", false);
        when(tableController.getAll()).thenReturn(List.of(tableCard));

        throwInFool.play(players);

        verify(playerController).setBinder(defender);
    }

    @Test
    void throwInFoolTest_whenPlayersNotBecomesWinner_thenTableIsCleared() {
        when(playerController.isGameOver()).thenReturn(false, true);
        when(playerController.isPlayerWinner(attacker, deckController.getDeck())).thenReturn(false);
        when(playerController.isPlayerWinner(defender, deckController.getDeck())).thenReturn(false);
        when(playerController.isPlayerWinner(thrower, deckController.getDeck())).thenReturn(false);
        when(throwMove.isThrowPossible(tableController.getAll(), thrower, playerController.getDefender())).thenReturn(false, true, false);

        throwInFool.play(players);

        verify(tableController).clear();
    }

    @Test
    void throwInFoolTest_whenPlayersNotBecomesWinner_thenFilledUpThePlayerHands() {
        when(playerController.isGameOver()).thenReturn(false, true);
        when(playerController.isPlayerWinner(attacker, deckController.getDeck())).thenReturn(false);
        when(playerController.isPlayerWinner(defender, deckController.getDeck())).thenReturn(false);
        when(playerController.isPlayerWinner(thrower, deckController.getDeck())).thenReturn(false);
        when(throwMove.isThrowPossible(tableController.getAll(), thrower, playerController.getDefender())).thenReturn(false, true, false);

        throwInFool.play(players);

        verify(deckController).fillUpTheHands(playerController.getThrowQueue(), playerController.getDefender());
    }

    @Test
    void throwInFoolTest_whenPlayersNotBecomesWinner_thenTurnIsChanged() {
        when(playerController.isGameOver()).thenReturn(false, true);
        when(playerController.isPlayerWinner(attacker, deckController.getDeck())).thenReturn(false);
        when(playerController.isPlayerWinner(defender, deckController.getDeck())).thenReturn(false);
        when(playerController.isPlayerWinner(thrower, deckController.getDeck())).thenReturn(false);
        when(throwMove.isThrowPossible(tableController.getAll(), thrower, playerController.getDefender())).thenReturn(false, true, false);

        throwInFool.play(players);

        verify(playerController).changeTurn();
    }

    @Test
    void throwInFoolTest_whenDeckIsNotEmpty_thenEmptyDeckMessageIsNotSent() {
        when(playerController.isGameOver()).thenReturn(false, true);
        when(deck.isEmpty()).thenReturn(false);
        String emptyDeckMessage = "\uD83D\uDE45 <b>Колода пуста!</b> \uD83D\uDE45";

        throwInFool.play(players);

        verify(messageService, never()).sendMessageToAll(players, emptyDeckMessage);
    }

    @Test
    void throwInFoolTest_whenDeckIsEmptyButEmptyDeckMessageWasSentAlready_thenEmptyDeckMessageIsNotSent() {
        when(playerController.isGameOver()).thenReturn(false, false, true);
        when(deck.isEmpty()).thenReturn(true);
        String emptyDeckMessage = "\uD83D\uDE45 <b>Колода пуста!</b> \uD83D\uDE45";

        throwInFool.play(players);

        verify(messageService, times(1)).sendMessageToAll(players, emptyDeckMessage);
    }

    @Test
    void throwInFoolTest_whenDeckIsEmptyAndEmptyDeckMessageWasNotSentYet_thenEmptyDeckMessageIstSent() {
        when(playerController.isGameOver()).thenReturn(false, true);
        when(deck.isEmpty()).thenReturn(true);
        String emptyDeckMessage = "\uD83D\uDE45 <b>Колода пуста!</b> \uD83D\uDE45";

        throwInFool.play(players);

        verify(messageService, times(1)).sendMessageToAll(players, emptyDeckMessage);
    }


}