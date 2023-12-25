package org.example.move;

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
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ThrowTest {
    Player thrower;
    Player defender;
    TableController tableControllerSpy;
    ArrayList<Player> playersForNotify;

    List<Card> thrownCards;
    @Mock
    TelegramBot bot;
    @Mock
    PlayerInputValidator playerInputValidator;
    @InjectMocks
    @Spy
    Throw throwMove;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

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
        when(playerInputValidator.askForCards(any(), any())).thenReturn(thrownCards);
        doReturn(true).when(throwMove).isThrowMoveCorrect(any(), any(), any());
        throwMove.throwMove(thrower, playersForNotify, tableControllerSpy, defender);

        verify(tableControllerSpy).addCardsToTable(thrownCards, thrower);
    }

    @Test
    void throwMove_whenThrowIsNotCorrect_thenSendWarning() {
        thrownCards = List.of(
                new Card("♥", "7", false));
        when(playerInputValidator.askForCards(any(), any())).thenReturn(thrownCards);
        doReturn(false).doReturn(true).when(throwMove).isThrowMoveCorrect(any(), any(), any());
        throwMove.throwMove(thrower, playersForNotify, tableControllerSpy, defender);

        bot.sendMessageTo(thrower, thrower.getName() + " , так не получится подкинуть.");

    }

    @Test
    void throwMove_whenThrowIsEmpty_thenSendNotificationAndDoNotAddCardsToTable() {
        thrownCards = List.of();
        when(playerInputValidator.askForCards(any(), any())).thenReturn(thrownCards);
        throwMove.throwMove(thrower, playersForNotify, tableControllerSpy, defender);

        verify(bot).sendMessageToAll(playersForNotify, thrower.getName() + " не будет подкидывать.");
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