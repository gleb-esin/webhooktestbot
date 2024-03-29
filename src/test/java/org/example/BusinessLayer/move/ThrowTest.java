package org.example.BusinessLayer.move;

import org.example.BusinessLayer.controller.DeckController;
import org.example.BusinessLayer.controller.PlayerController;
import org.example.BusinessLayer.controller.TableController;
import org.example.EntityLayer.Card;
import org.example.EntityLayer.Player;
import org.example.EntityLayer.Table;
import org.example.ServiseLayer.services.MessageService;
import org.example.ServiseLayer.services.PlayerInputValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ThrowTest {
    AutoCloseable closeable;
    @Mock
    Table table;
    @Mock
    Player thrower;
    @Mock
    Player defender;
    @Mock
    TableController tableController;
    @Mock
    PlayerController playerController;
    @Mock
    DeckController deckController;
    @Mock
    PlayerInputValidator playerInputValidator;
    @Mock
    MessageService messageService;
    @InjectMocks
    Throw throwMove;
    List<Card> beatenCards = new ArrayList<>();
    List<Card> allCards = new ArrayList<>();

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        when(thrower.getRole()).thenReturn("attacker");

        beatenCards.addAll(List.of(new Card("♠", "7", true), new Card("♣", "7", false)));
        when(table.getBeatenCards()).thenReturn(beatenCards);

        when(tableController.getTable()).thenReturn(table);

        allCards.addAll(beatenCards);
        when(tableController.getAll()).thenReturn(allCards);


        when(playerController.getDefender()).thenReturn(defender);
        when(defender.getPlayerHand()).thenReturn(List.of(new Card("♠", "9", true), new Card("♦", "9", true)));
        when(defender.getRole()).thenReturn("defender");

    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void throwMove_whenThrowerThrowValidCardsAndDefenderHasNoEmptyHand_thenCardsAreAddedAndDefenceIsNeeded() {
        List<Card> thrownCards = List.of(
                new Card("♥", "7", false),
                new Card("♦", "7", false)
        );
        when(playerInputValidator.askForCards(thrower)).thenReturn(thrownCards);
        allCards.addAll(thrownCards);

        when(thrower.getPlayerHand())
                //initial
                .thenReturn(List.of(
                        new Card("♥", "7", false),
                        new Card("♦", "7", false),
                        new Card("♣", "8", false)))
                //after first throw
                .thenReturn(List.of(
                        new Card("♣", "8", false)));
        when(table.getUnbeatenCards())
                //initial
                .thenReturn(new ArrayList<>())
                //after first throw
                .thenReturn(thrownCards);

        boolean isDefenceNeeded = throwMove.move(thrower, playerController, tableController, deckController);

        verify(tableController, times(1)).addCardsToTable(any(), any());
        assertTrue(isDefenceNeeded);
    }


    @Test
    void throwMove_whenThrowerThrowValidCardsAndDefenderHasEmptyHand_thenCardsAreNotAddedAndDefenceIsNotNeeded() {
        when(defender.getPlayerHand()).thenReturn(new ArrayList<>());
        when(thrower.getPlayerHand())
                .thenReturn(List.of(
                        new Card("♥", "7", false),
                        new Card("♦", "7", false),
                        new Card("♣", "8", false)))
                .thenReturn(List.of(
                        new Card("♣", "8", false)));

        boolean isDefenceNeeded = throwMove.move(thrower, playerController, tableController, deckController);

        verify(tableController, never()).addCardsToTable(any(), any());
        assertFalse(isDefenceNeeded);
    }

    @Test
    void throwMove_whenThrowerHasNoThrowableCardsAndDefenderHasNoEmptyHand_thenCardsAreNotAddedAndDefenceIsNotNeeded() {
        when(thrower.getPlayerHand())
                .thenReturn(List.of(
                        new Card("♣", "8", false)));

        boolean isDefenceNeeded = throwMove.move(thrower, playerController, tableController, deckController);

        verify(tableController, never()).addCardsToTable(any(), any());
        assertFalse(isDefenceNeeded);
    }

    @Test
    void throwMove_whenThrowerThrowMoreCadsThanDefendersHandContains_thenCardsAreNotAddedAtNonValidAttempt() {
        List<Card> thrownCardsNonValid = List.of(
                new Card("♥", "7", false),
                new Card("♦", "7", false),
                new Card("♣", "8", false)
        );
        List<Card> thrownCardsValid = List.of(
                new Card("♥", "7", false),
                new Card("♦", "7", false)
        );
        when(playerInputValidator.askForCards(thrower))
                .thenReturn(thrownCardsNonValid)
                .thenReturn(thrownCardsValid);
        allCards.addAll(thrownCardsValid);

        when(thrower.getPlayerHand())
                //initial
                .thenReturn(List.of(
                        new Card("♥", "7", false),
                        new Card("♦", "7", false),
                        new Card("♣", "8", false)))
                //after valid throw
                .thenReturn(List.of(
                        new Card("♣", "8", false)));
        when(table.getUnbeatenCards())
                //initial
                .thenReturn(new ArrayList<>())
                //after valid throw
                .thenReturn(thrownCardsValid);

        throwMove.move(thrower, playerController, tableController, deckController);
        //we make two attempts to throw but cards added only once
        verify(tableController, times(1)).addCardsToTable(any(), any());
    }

    @Test
    void throwMove_whenThrowerThrowNonThrowableCardsAndDefenderHasNoEmptyHand_thenCardsAreNotAddedAtNonValidAttempt() {
        List<Card> thrownCardsNonValid = List.of(
                new Card("♣", "8", false)
        );
        List<Card> thrownCardsValid = List.of(
                new Card("♥", "7", false),
                new Card("♦", "7", false)
        );
        when(playerInputValidator.askForCards(thrower))
                .thenReturn(thrownCardsNonValid)
                .thenReturn(thrownCardsValid);
        allCards.addAll(thrownCardsValid);

        when(thrower.getPlayerHand())
                //initial
                .thenReturn(List.of(
                        new Card("♥", "7", false),
                        new Card("♦", "7", false),
                        new Card("♣", "8", false)))
                //after valid throw
                .thenReturn(List.of(
                        new Card("♣", "8", false)));
        when(table.getUnbeatenCards())
                //initial
                .thenReturn(new ArrayList<>())
                //after valid throw
                .thenReturn(thrownCardsValid);

        throwMove.move(thrower, playerController, tableController, deckController);
        //we make two attempts to throw but cards added only once
        verify(tableController, times(1)).addCardsToTable(any(), any());
    }

    //
    @Test
    void throwMove_whenThrowerThrowNoCards_thenSendNotificationAndCardsAreNotAddedAndDefenceIsNotNeeded() {
        List<Card> emptyInput = List.of();
        when(playerInputValidator.askForCards(thrower))
                .thenReturn(emptyInput);
        when(thrower.getPlayerHand())
                .thenReturn(List.of(
                        new Card("♥", "7", false),
                        new Card("♦", "7", false),
                        new Card("♣", "8", false)));
        String notification = thrower.getName() + " не будет подкидывать.";

        boolean isDefenceNeeded = throwMove.move(thrower, playerController, tableController, deckController);

        verify(messageService).sendMessageToAll(any(), eq(notification));
        verify(tableController, never()).addCardsToTable(any(), any());
        assertFalse(isDefenceNeeded);
    }

    @Test
    void throwMove_whenThrowerBecameWinner_thenDefenceIsNotNeeded() {
        List<Card> thrownCards = List.of(
                new Card("♥", "7", false),
                new Card("♦", "7", false)
        );
        when(playerInputValidator.askForCards(thrower)).thenReturn(thrownCards);
        allCards.addAll(thrownCards);

        when(thrower.getPlayerHand())
                //initial
                .thenReturn(List.of(
                        new Card("♥", "7", false),
                        new Card("♦", "7", false)));

        when(playerController.isPlayerWinner(any(), any())).thenReturn(true);

        boolean isDefenceNeeded = throwMove.move(thrower, playerController, tableController, deckController);

        verify(tableController, times(1)).addCardsToTable(any(), any());
        assertFalse(isDefenceNeeded);
    }

    @Test
    void throwMove_whenThrowerThrowValidCardsAndDefenderHasNoEmptyHandButHasBinderRole_thenCardsAreAddedAndDefenceIsNotNeeded() {
        when(defender.getRole()).thenReturn("binder");
        List<Card> thrownCards = List.of(
                new Card("♥", "7", false),
                new Card("♦", "7", false)
        );
        when(playerInputValidator.askForCards(thrower)).thenReturn(thrownCards);
        allCards.addAll(thrownCards);

        when(thrower.getPlayerHand())
                //initial
                .thenReturn(List.of(
                        new Card("♥", "7", false),
                        new Card("♦", "7", false),
                        new Card("♣", "8", false)))
                //after first throw
                .thenReturn(List.of(
                        new Card("♣", "8", false)));
        when(table.getUnbeatenCards())
                //initial
                .thenReturn(new ArrayList<>())
                //after first throw
                .thenReturn(thrownCards);

        boolean isDefenceNeeded = throwMove.move(thrower, playerController, tableController, deckController);

        verify(tableController, times(1)).addCardsToTable(any(), any());
        assertFalse(isDefenceNeeded);
    }

    @Test
    void isThrowPossible_whenThrowerReallyDontWantThrow_thenThrowIsNotPossible() throws NoSuchFieldException, IllegalAccessException {
        Field throwerWantThrow = throwMove.getClass().getDeclaredField("throwerWantThrow");
        throwerWantThrow.setAccessible(true);
        throwerWantThrow.set(throwMove, false);

        boolean isThrowPossible = throwMove.isThrowPossible(beatenCards, thrower, defender);

        assertFalse(isThrowPossible);
    }

    @Test
    void isThrowPossible_whenDefenderHasEmptyHand_thenThrowIsNotPossible() {
        when(defender.getPlayerHand()).thenReturn(new ArrayList<>());

        boolean isThrowPossible = throwMove.isThrowPossible(beatenCards, thrower, defender);

        assertFalse(isThrowPossible);
    }

    @Test
    void isThrowPossible_whenThrowerHasEmptyHand_thenThrowIsNotPossible() {
        when(thrower.getPlayerHand()).thenReturn(new ArrayList<>());

        boolean isThrowPossible = throwMove.isThrowPossible(beatenCards, thrower, defender);

        assertFalse(isThrowPossible);
    }

    @Test
    void isThrowPossible_whenThrowerWhichIsNotOnTable_thenThrowIsNotPossible() {
        when(thrower.getPlayerHand())
                .thenReturn(List.of(
                        new Card("♣", "8", false)));

        boolean isThrowPossible = throwMove.isThrowPossible(beatenCards, thrower, defender);

        assertFalse(isThrowPossible);

    }

    @Test
    void isThrowPossible_whenThrowerHasCardWhichIsOnTable_thenThrowIsNotPossible() {
        when(thrower.getPlayerHand())
                .thenReturn(List.of(
                        new Card("♣", "7", false)));

        boolean isThrowPossible = throwMove.isThrowPossible(beatenCards, thrower, defender);

        assertTrue(isThrowPossible);
    }
}