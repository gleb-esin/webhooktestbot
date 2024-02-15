package org.example.BusinessLayer.move;

import org.example.BusinessLayer.controller.PlayerController;
import org.example.BusinessLayer.controller.TableController;
import org.example.EntityLayer.Card;
import org.example.EntityLayer.Player;
import org.example.ServiseLayer.services.MessageService;
import org.example.ServiseLayer.services.MessageServiceImpl;
import org.example.ServiseLayer.services.PlayerInputValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AbstractDefenceTest {
    AutoCloseable closeable;
    @Mock
    PlayerController playerController;
    @Mock
    MessageServiceImpl messageService;
    @Spy
    @InjectMocks
    AbstractDefenceImpl defence;
    Player defender;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        defender = new Player(1L, "Defender");
        when(playerController.getDefender()).thenReturn(defender);

    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
    @Test
    void init() {
        defence.init(playerController);
        verify(messageService).sendMessageToAll(playerController.getPlayers(),
                "\uD83D\uDEE1 Отбивается " + playerController.getDefender().getName() + " \uD83D\uDEE1");
        verify(messageService).sendMessageTo(playerController.getDefender(), playerController.getDefender().toString());
    }

    @Test
    void isDefenceCorrect_whenDefenderHasNotEnoughCards_ThenReturnFalse() {
        List<Card> unbeatenCards = List.of(new Card("♠", "6", false), new Card("♠", "7", false));
        List<Card> defenderCards = List.of(new Card("♠", "8", false));

        boolean expected = defence.isDefenceCorrect(unbeatenCards, defenderCards);

        assertFalse(expected);
    }

    @Test
    void isDefenceCorrect_whenDefenderHasEnoughCardsOfHigherValue_ThenReturnTrue() {
        List<Card> unbeatenCards = List.of(new Card("♠", "6", false));
        List<Card> defenderCards = List.of(new Card("♠", "8", false), new Card("♠", "7", false));

        boolean expected = defence.isDefenceCorrect(unbeatenCards, defenderCards);

        assertTrue(expected);
    }

    @Test
    void isDefenceCorrect_whenDefenderHasEnoughCardsOfLessValue_ThenReturnFalse() {
        List<Card> unbeatenCards = List.of(new Card("♠", "8", false));
        List<Card> defenderCards = List.of(new Card("♠", "6", false), new Card("♠", "7", false));

        boolean expected = defence.isDefenceCorrect(unbeatenCards, defenderCards);

        assertFalse(expected);
    }

    @Test
    void isDefenceCorrect_when() {
        List<Card> unbeatenCards = List.of(new Card("♠", "6", false), new Card("♠", "7", false));
        List<Card> defenderCards = List.of(new Card("♠", "8", false), new Card("♣", "6", false));

        boolean expected = defence.isDefenceCorrect(unbeatenCards, defenderCards);

        assertFalse(expected);
    }
}

class AbstractDefenceImpl extends AbstractDefence{

    public AbstractDefenceImpl(MessageService messageService, PlayerInputValidator playerInputValidator) {
        super(messageService, playerInputValidator);
    }

    @Override
    public void move(PlayerController playerController, TableController tableController) {

    }
}