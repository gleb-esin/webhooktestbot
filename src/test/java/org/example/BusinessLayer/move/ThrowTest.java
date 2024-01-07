package org.example.BusinessLayer.move;

import org.example.EntityLayer.Card;
import org.example.EntityLayer.Player;
import org.example.EntityLayer.Suit;
import org.example.ServiseLayer.services.MessageService;
import org.example.ServiseLayer.services.PlayerInputValidator;
import org.example.BusinessLayer.controller.TableController;
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
import static org.mockito.Mockito.*;

class ThrowTest {
    AutoCloseable closeable;
    Player thrower;
    Player defender;
    TableController tableControllerSpy;
    ArrayList<Player> playersForNotify;

    List<Card> thrownCards;
    @Mock
    MessageService messageService;
    @Mock
    PlayerInputValidator playerInputValidator;
    @InjectMocks
    @Spy
    Throw throwMove;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        thrower = new Player(1L, "thrower");
        defender = new Player(2L, "defender");
        thrower.setRole("thrower");
        playersForNotify = new ArrayList<>();
        playersForNotify.add(thrower);

        tableControllerSpy = spy(new TableController(new Suit("♠", true)));
        tableControllerSpy.getTable().getBeatenCards().addAll(List.of(
                new Card("♠", "7", true),
                new Card("♣", "7", false)));
        defender.getPlayerHand().add(new Card("♠", "9", true));
        thrower.getPlayerHand().addAll(
                List.of(
                        new Card("♥", "7", false),
                        new Card("♦", "7", false),
                        new Card("♣", "8", false)));

    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void isThrowMoveCorrect_whenThrownCardsSizeIsNotGreaterThanDefenderPlayerHandSizeAndValuesOfThrownCardsIsEqualToBeatenCards_thenTrue() {
        thrownCards = List.of(
                new Card("♥", "7", false));

        boolean actual = throwMove.isThrowMoveCorrect(tableControllerSpy.getAll(), thrownCards, defender);

        assertTrue(actual);
    }

    @Test
    void isThrowMoveCorrect_whenThrownCardsIsEmpty_thenTrue() {
        thrownCards = List.of();
        boolean actual = throwMove.isThrowMoveCorrect(tableControllerSpy.getAll(), thrownCards, defender);

        assertTrue(actual);
    }
    @Test
    void isThrowMoveCorrect_whenThrownCardsSizeIsGreaterThanDefenderPlayerHandSize_thenFalse() {
        thrownCards = List.of(
                new Card("♥", "7", false),
                new Card("♦", "7", false));

        boolean actual = throwMove.isThrowMoveCorrect(tableControllerSpy.getAll(), thrownCards, defender);

        assertFalse(actual);
    }

    @Test
    void isThrowMoveCorrect_whenValuesOfThrownCardsIsNotEqualToBeatenCards_thenFalse() {
        thrownCards = List.of(
                new Card("♥", "9", false));

        boolean actual = throwMove.isThrowMoveCorrect(tableControllerSpy.getAll(), thrownCards, defender);

        assertFalse(actual);
    }


    @Test
    void throwMove_whenThrowIsCorrect_thenAddCardToTable() {
        thrownCards = List.of(
                new Card("♥", "7", false));
        when(playerInputValidator.askForCards(any())).thenReturn(thrownCards);
        doReturn(true).when(throwMove).isThrowMoveCorrect(any(), any(), any());
        throwMove.throwMove(thrower, playersForNotify, tableControllerSpy, defender);

        verify(tableControllerSpy).addCardsToTable(thrownCards, thrower);
    }

    @Test
    void throwMove_whenThrowIsNotCorrect_thenSendWarning() {
        thrownCards = List.of(
                new Card("♥", "7", false));
        when(playerInputValidator.askForCards(any())).thenReturn(thrownCards);
        doReturn(false).doReturn(true).when(throwMove).isThrowMoveCorrect(any(), any(), any());
        throwMove.throwMove(thrower, playersForNotify, tableControllerSpy, defender);

        messageService.sendMessageTo(thrower, thrower.getName() + " , так не получится подкинуть.");

    }

    @Test
    void throwMove_whenThrowIsEmpty_thenSendNotificationAndDoNotAddCardsToTable() {
        thrownCards = List.of();
        when(playerInputValidator.askForCards(any())).thenReturn(thrownCards);
        throwMove.throwMove(thrower, playersForNotify, tableControllerSpy, defender);

        verify(messageService).sendMessageToAll(playersForNotify, thrower.getName() + " не будет подкидывать.");
        assertEquals(0, tableControllerSpy.getTable().getUnbeatenCards().size());
    }
    @Test
    void isThrowPossible_whenThrownCardsValuesIsEqualToBeatenCards_thenTrue() {
        thrownCards = List.of(
                new Card("♥", "7", false));
        boolean actual = throwMove.isThrowPossible(tableControllerSpy.getAll(), thrownCards);

        assertTrue(actual);
    }

    @Test
    void isThrowPossible_whenThrownCardsValuesIsNotEqualToBeatenCards_thenFalse() {
        thrownCards = List.of(
                new Card("♥", "10", false));
        boolean actual = throwMove.isThrowPossible(tableControllerSpy.getAll(), thrownCards);

        assertFalse(actual);
    }
}