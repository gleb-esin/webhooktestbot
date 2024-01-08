package org.example.ServiseLayer.services;

import org.example.EntityLayer.Card;
import org.example.EntityLayer.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PlayerInputValidatorTest {
    AutoCloseable closeable;

    @Mock
    MessageService messageService;
    Player player = new Player(1L, "test");
    @InjectMocks
    PlayerInputValidator playerInputValidator;
    List<Card> expected = List.of(new Card("♠", "6", false), new Card("♠", "7", false));

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        player.getPlayerHand().add(new Card("♠", "6", false));
        player.getPlayerHand().add(new Card("♠", "7", false));
        player.getPlayerHand().add(new Card("♣", "6", false));
    }
    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void askForCards_whenInputIsCorrectAndSeparatedBySpace_thenSelectedCardsAreReturned() {
        when(messageService.receiveMessageFrom(player)).thenReturn("1 2");
        List<Card> actual = playerInputValidator.askForCards(player);

        assertEquals(expected, actual);
    }

    @Test
    void askForCards_whenInputIsCorrectAndSeparatedByComma_thenSelectedCardsAreReturned() {
        when(messageService.receiveMessageFrom(player)).thenReturn("1,2");
        List<Card> actual = playerInputValidator.askForCards(player);

        assertEquals(expected, actual);
    }

    @Test
    void askForCards_whenInputIsCorrectAndSeparatedByDot_thenSelectedCardsAreReturned() {
        when(messageService.receiveMessageFrom(player)).thenReturn("1.2");
        List<Card> actual = playerInputValidator.askForCards(player);

        assertEquals(expected, actual);
    }

    @Test
    void askForCards_whenInputIsCorrectAndSeparatedByHyphen_thenSelectedCardsAreReturned() {
        when(messageService.receiveMessageFrom(player)).thenReturn("1-2");
        List<Card> actual = playerInputValidator.askForCards(player);

        assertEquals(expected, actual);
    }

    @Test
    void askForCards_whenInputIsCorrectAndSeparatedByExclamation_thenSelectedCardsAreReturned() {
        when(messageService.receiveMessageFrom(player)).thenReturn("1!2");
        List<Card> actual = playerInputValidator.askForCards(player);

        assertEquals(expected, actual);
    }

    @Test
    void askForCards_whenInputIsCorrectAndSeparatedByAt_thenSelectedCardsAreReturned() {
        when(messageService.receiveMessageFrom(player)).thenReturn("1@2");
        List<Card> actual = playerInputValidator.askForCards(player);

        assertEquals(expected, actual);
    }

    @Test
    void askForCards_whenInputIsCorrectAndSeparatedByHash_thenSelectedCardsAreReturned() {
        when(messageService.receiveMessageFrom(player)).thenReturn("1#2");
        List<Card> actual = playerInputValidator.askForCards(player);

        assertEquals(expected, actual);
    }
    @Test
    void askForCards_whenInputIsCorrectAndSeparatedByDollar_thenSelectedCardsAreReturned() {
        when(messageService.receiveMessageFrom(player)).thenReturn("1$2");
        List<Card> actual = playerInputValidator.askForCards(player);

        assertEquals(expected, actual);
    }

    @Test
    void askForCards_whenInputIsCorrectAndSeparatedByPercent_thenSelectedCardsAreReturned() {
        when(messageService.receiveMessageFrom(player)).thenReturn("1%2");
        List<Card> actual = playerInputValidator.askForCards(player);

        assertEquals(expected, actual);
    }

    @Test
    void askForCards_whenInputIsCorrectAndSeparatedByAmpersand_thenSelectedCardsAreReturned() {
        when(messageService.receiveMessageFrom(player)).thenReturn("1&2");
        List<Card> actual = playerInputValidator.askForCards(player);

        assertEquals(expected, actual);
    }

    @Test
    void askForCards_whenInputIsCorrectAndSeparatedByStar_thenSelectedCardsAreReturned() {
        when(messageService.receiveMessageFrom(player)).thenReturn("1*2");
        List<Card> actual = playerInputValidator.askForCards(player);

        assertEquals(expected, actual);
    }

    @Test
    void askForCards_whenInputIsCorrectAndSeparatedByParenthesesOpen_thenSelectedCardsAreReturned() {
        when(messageService.receiveMessageFrom(player)).thenReturn("1(2");
        List<Card> actual = playerInputValidator.askForCards(player);

        assertEquals(expected, actual);
    }

    @Test
    void askForCards_whenInputIsCorrectAndSeparatedByParenthesesClose_thenSelectedCardsAreReturned() {
        when(messageService.receiveMessageFrom(player)).thenReturn("1)2");
        List<Card> actual = playerInputValidator.askForCards(player);

        assertEquals(expected, actual);
    }

    @Test
    void askForCards_whenInputIsCorrectAndSeparatedByUnderScore_thenSelectedCardsAreReturned() {
        when(messageService.receiveMessageFrom(player)).thenReturn("1_2");
        List<Card> actual = playerInputValidator.askForCards(player);

        assertEquals(expected, actual);
    }

    @Test
    void askForCards_whenInputIsCorrectAndSeparatedByEqual_thenSelectedCardsAreReturned() {
        when(messageService.receiveMessageFrom(player)).thenReturn("1=2");
        List<Card> actual = playerInputValidator.askForCards(player);

        assertEquals(expected, actual);
    }

    @Test
    void askForCards_whenInputIsCorrectAndSeparatedByPlus_thenSelectedCardsAreReturned() {
        when(messageService.receiveMessageFrom(player)).thenReturn("1+2");
        List<Card> actual = playerInputValidator.askForCards(player);

        assertEquals(expected, actual);
    }
    @Test
    void askForCards_whenInputIsEmpty_thenReturnedEmptyList() {
        when(messageService.receiveMessageFrom(player)).thenReturn("0 2");

        List<Card> actual = playerInputValidator.askForCards(player);

        assertTrue(actual.isEmpty());
    }

    @Test
    void askForCards_whenInputContainsNotExistingCardIndex_thenSendWarningMessage() {
        when(messageService.receiveMessageFrom(player)).thenReturn("1 4").thenReturn("1 2");

        playerInputValidator.askForCards(player);

        verify(messageService).sendMessageTo(player, "Неверный ввод. Попробуйте ещё раз:");
    }

    @Test
    void askForCards_whenInputContainsNegativeCardIndex_thenReturnPositiveIndex() {
        when(messageService.receiveMessageFrom(player)).thenReturn("-1 2");

        List<Card> actual = playerInputValidator.askForCards(player);

        assertEquals(expected, actual);
    }

    @Test
    void askForCards_whenInputIsEmpty_thenReturnPositiveIndex() {
        when(messageService.receiveMessageFrom(player)).thenReturn("").thenReturn("1 2");

        playerInputValidator.askForCards(player);

        verify(messageService).sendMessageTo(player, "Неверный ввод. Попробуйте ещё раз:");

    }

    @Test
    void askForCards_whenInputContainsMoreIndexesThanContainsPlayerHand_thenReturnPositiveIndex() {
        when(messageService.receiveMessageFrom(player)).thenReturn("1 2 3 4 5").thenReturn("1 2");

        playerInputValidator.askForCards(player);

        verify(messageService).sendMessageTo(player, "Неверный ввод. Попробуйте ещё раз:");

    }
}