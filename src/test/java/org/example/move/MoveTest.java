package org.example.move;

import org.example.model.Card;
import org.example.model.Player;
import org.example.monitor.UpdateMonitor;
import org.example.network.TelegramBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MoveTest {
    Player player;
    TelegramBot bot;
    Move moveImpl;

    @BeforeEach
    void setUp() {
        player = new Player(1L, "Player");
        bot = mock(TelegramBot.class);
        player.getPlayerHand().add(new Card("♣", "6", false));
        player.getPlayerHand().add(new Card("♥", "6", false));
        player.getPlayerHand().add(new Card("♦", "6", false));
        player.getPlayerHand().add(new Card("♣", "9", false));
        player.getPlayerHand().add(new Card("♠", "6", true));
        player.getPlayerHand().add(new Card("♠", "7", true));

        moveImpl = new MoveImpl();
    }

    @Test
    void askForCards() {
        String cardIndexes = "1 3 2";
        UpdateMonitor updateMonitorMock = Mockito.mock(UpdateMonitor.class);
        Move spy = Mockito.spy(moveImpl);
        when(bot.getUpdateMonitor()).thenReturn(updateMonitorMock);
        when(updateMonitorMock.getMessage(player.getChatID())).thenReturn(cardIndexes);

        List<Card> cards = spy.askForCards(player, bot);
        List<Card> expectedCards =List.of(new Card("♣", "6", false), new Card("♥", "6", false), new Card("♦", "6", false));

        assertEquals(expectedCards, cards);
    }

    private class MoveImpl implements Move {
    }
}
