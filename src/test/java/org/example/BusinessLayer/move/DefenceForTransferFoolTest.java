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
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class DefenceForTransferFoolTest {
    AutoCloseable closeable;
    Player attacker;
    Player defender;
    ArrayList<Card> defenderCardsCorrect;

    TableController tableController;
    @Spy
    PlayerController playerController;
    ArrayList<Card> cards;
    @Mock
    MessageService messageService;
    @Mock
    PlayerInputValidator playerInputValidator;
    @Spy
    @InjectMocks
    DefenceForTransferFool defence;
    List<Player> playersForNotify;
    List<Card> unbeatenCards;
    List<Card> defenderCards;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        TableController spy = new TableController(new Table());
        tableController = spy(spy);
        attacker = new Player(1L, "attacker");
        defender = new Player(2L, "defender");
        playersForNotify = List.of(attacker, defender);
        tableController.setTrump(new Suit("♠", true));
        playerController.setPlayers(playersForNotify);
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
        unbeatenCards = new ArrayList<>();
        defenderCards = new ArrayList<>();
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void move_whenCalled_thenIsDefenceCorrectIsCalled() {
        doReturn(false).when(defence).isDefenceCorrect(any(), any());
        doReturn(false).when(defence).isTransferPossible(any(), any());

        defence.move(playerController, tableController);

        verify(defence).isDefenceCorrect(any(), any());
    }

    @Test
    void move_whenCalled_thenIsTransferPossibleIsCalled() {
        doReturn(false).when(defence).isDefenceCorrect(any(), any());
        doReturn(false).when(defence).isTransferPossible(any(), any());

        defence.move(playerController, tableController);

        verify(defence).isTransferPossible(any(), any());
    }

    @Test
    void move_whenMoveIsNotPossible_thenDefenderGetsRoleAsBinder() {
        doReturn(false).when(defence).isDefenceCorrect(any(), any());
        doReturn(false).when(defence).isTransferPossible(any(), any());

        defence.move(playerController, tableController);

        assertEquals(defender.getRole(), "binder");
    }

    @Test
    void move_whenMoveIsNotPossible_thenSetBinderIsCalled() {
        doReturn(false).when(defence).isDefenceCorrect(any(), any());
        doReturn(false).when(defence).isTransferPossible(any(), any());

        defence.move(playerController, tableController);

        verify(playerController).setBinder(defender);
    }

    @Test
    void move_whenMoveIsPossible_thenAskFoCardsIsCalled() {
        doReturn(true).when(defence).isDefenceCorrect(any(), any());
        doReturn(true).when(defence).isTransferPossible(any(), any());
        defence.move(playerController, tableController);

        verify(playerInputValidator).askForCards(defender);
    }

    @Test
    void move_whenMoveIsPossibleAndCardsIsEmpty_thenDefendersRoleBecomesBinder() {
        when(playerInputValidator.askForCards(defender)).thenReturn(List.of());

        defence.move(playerController, tableController);

        assertEquals(defender.getRole(), "binder");
    }

    @Test
    void move_whenMoveIsPossibleAndCardsIsEmpty_thenSetBinderIsCalled() {
        when(playerInputValidator.askForCards(defender)).thenReturn(List.of());

        defence.move(playerController, tableController);

        verify(playerController).setBinder(defender);
    }

    @Test
    void move_whenMoveIsPossibleAndCardsIsNotEmpty_thenIsDefenceCorrectIsCalledForSecondTime() {
        doReturn(true).when(defence).isDefenceCorrect(any(), any());
        doReturn(true).when(defence).isTransferPossible(any(), any());
        when(playerInputValidator.askForCards(defender)).thenReturn(List.of(mock(Card.class)));

        defence.move(playerController, tableController);

        verify(defence, times(2)).isDefenceCorrect(any(), any());
    }

    @Test
    void move_whenMoveIsPossibleAndCardsIsNotEmpty_thenIsTransferCorrectIsCalled() {
        doReturn(true).when(defence).isDefenceCorrect(any(), any());
        doReturn(true).when(defence).isTransferPossible(any(), any());
        when(playerInputValidator.askForCards(defender)).thenReturn(List.of(mock(Card.class)));

        defence.move(playerController, tableController);

        verify(defence).isTransferCorrect(any(), any());
    }

    @Test
    void move_whenMoveIsIsNotCorrectAndCardsIsEmpty_thenDefenderGetsRoleAsBinder() {
        doReturn(true).doReturn(false).when(defence).isDefenceCorrect(any(), any());
        doReturn(true).doReturn(false).when(defence).isTransferPossible(any(), any());
        when(playerInputValidator.askForCards(defender)).thenReturn(List.of());

        defence.move(playerController, tableController);

        assertEquals(defender.getRole(), "binder");
    }

    @Test
    void move_whenMoveIsIsNotCorrectAndCardsIsEmpty_thenSetBinderIsCalled() {
        doReturn(true).doReturn(false).when(defence).isDefenceCorrect(any(), any());
        doReturn(true).doReturn(false).when(defence).isTransferPossible(any(), any());
        when(playerInputValidator.askForCards(defender)).thenReturn(List.of());

        defence.move(playerController, tableController);

        verify(playerController).setBinder(defender);
    }

    @Test
    void move_whenMoveIsNotCorrectAndCardsIsNotEmpty_thenIsDefenceCorrectIsCalledForThirdTime() {
        doReturn(true).doReturn(false).doReturn(true).when(defence).isDefenceCorrect(any(), any());
        doReturn(true).doReturn(false).doReturn(true).when(defence).isTransferPossible(any(), any());
        when(playerInputValidator.askForCards(defender)).thenReturn(List.of(mock(Card.class)));

        defence.move(playerController, tableController);

        verify(defence, times(3)).isDefenceCorrect(any(), any());
    }

    @Test
    void move_whenMoveIsNotCorrectAndCardsIsNotEmpty_thenIsTransferCorrectIsCalledForTheSecondTime() {
        doReturn(true).doReturn(false).doReturn(true).when(defence).isDefenceCorrect(any(), any());
        doReturn(true).doReturn(false).doReturn(true).when(defence).isTransferPossible(any(), any());
        when(playerInputValidator.askForCards(defender)).thenReturn(List.of(mock(Card.class)));

        defence.move(playerController, tableController);

        verify(defence, times(2)).isTransferCorrect(any(), any());
    }

    @Test
    void move_whenMoveIsCorrectAndIsTransferCorrect_thenDefenderGetsRoleAsCardsTransfer() {
        doReturn(true).doReturn(true).when(defence).isDefenceCorrect(any(), any());
        doReturn(true).doReturn(true).when(defence).isTransferCorrect(any(), any());
        when(playerInputValidator.askForCards(defender)).thenReturn(List.of(mock(Card.class)));

        defence.move(playerController, tableController);

        assertEquals("cardsTransfer", defender.getRole());
    }

    @Test
    void move_whenMoveIsCorrect_thenAddCardsToTableIsCalled() {
        doReturn(true).doReturn(true).when(defence).isDefenceCorrect(any(), any());
        doReturn(true).doReturn(true).when(defence).isTransferCorrect(any(), any());
        when(playerInputValidator.askForCards(defender)).thenReturn(List.of(new Card("♠", "9", true)));

        defence.move(playerController, tableController);

        verify(tableController).addCardsToTable(List.of(new Card("♠", "9", true)), defender);
    }

    @Test
    void move_whenMoveIsCorrect_thenSendMessageToAllWithTable() {
        doReturn(true).doReturn(true).when(defence).isDefenceCorrect(any(), any());
        doReturn(true).doReturn(true).when(defence).isTransferCorrect(any(), any());
        when(playerInputValidator.askForCards(defender)).thenReturn(List.of(new Card("♠", "9", true)));

        defence.move(playerController, tableController);
        Table table = tableController.getTable();
        verify(messageService).sendMessageToAll(any(), eq(table.toString()));
    }

    @Test
    void move_whenDefendersRoleIsCardsTransfer_thenSendNotification() {
        doReturn(true).doReturn(true).when(defence).isDefenceCorrect(any(), any());
        doReturn(true).doReturn(true).when(defence).isTransferCorrect(any(), any());
        when(playerInputValidator.askForCards(defender)).thenReturn(List.of(new Card("♠", "9", true)));

        defence.move(playerController, tableController);

        verify(messageService).sendMessageToAll(any(), eq(defender.getName() + " перевел карты!"));
    }

    @Test
    void move_whenDefendersRoleIsNotCardsTransfer_thenSendNotification() {
        doReturn(true).doReturn(true).when(defence).isDefenceCorrect(any(), any());
        doReturn(false).doReturn(false).when(defence).isTransferCorrect(any(), any());
        when(playerInputValidator.askForCards(defender)).thenReturn(List.of(new Card("♠", "9", true)));

        defence.move(playerController, tableController);

        verify(messageService).sendMessageToAll(any(), eq("Карты отбиты!"));
    }


    @Test
    void isTransferPossible_whenUnbeatenCardsValueIsNitEquals_thenReturnFalse() {
        unbeatenCards.add(new Card("♠", "8", true));
        unbeatenCards.add(new Card("♠", "9", true));
        defenderCards.add(new Card("♣", "8", false));

        boolean expected = defence.isTransferPossible(unbeatenCards, defenderCards);

        assertFalse(expected);
    }

    @Test
    void isTransferPossible_whenUnbeatenCardsValueIsEqualsToDefenderCards_thenReturnTrue() {
        unbeatenCards.add(new Card("♠", "8", true));
        unbeatenCards.add(new Card("♥", "8", false));
        defenderCards.add(new Card("♣", "8", false));
        defenderCards.add(new Card("♥", "9", false));

        boolean expected = defence.isTransferPossible(unbeatenCards, defenderCards);

        assertTrue(expected);
    }

    @Test
    void isTransferCorrect_whenUnbeatenCardsValuesIsNotEqualsToDefenderCards_thenReturnFalse() {
        unbeatenCards.add(new Card("♠", "8", true));
        unbeatenCards.add(new Card("♥", "8", false));
        defenderCards.add(new Card("♣", "8", false));
        defenderCards.add(new Card("♥", "9", false));

        boolean expected = defence.isTransferCorrect(unbeatenCards, defenderCards);

        assertFalse(expected);
    }

    @Test
    void isTransferCorrect_whenUnbeatenCardsValuesIsEqualsToDefenderCards_thenReturnTrue() {
        unbeatenCards.add(new Card("♠", "8", true));
        unbeatenCards.add(new Card("♥", "8", false));
        defenderCards.add(new Card("♣", "8", false));

        boolean expected = defence.isTransferCorrect(unbeatenCards, defenderCards);

        assertTrue(expected);
    }
}