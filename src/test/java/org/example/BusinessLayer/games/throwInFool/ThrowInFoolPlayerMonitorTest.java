package org.example.BusinessLayer.games.throwInFool;


import org.example.BusinessLayer.games.ThrowInFoolPlayerMonitor;
import org.example.EntityLayer.Player;
import org.example.ServiseLayer.factories.GameFactory;
import org.example.ServiseLayer.services.MessageService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class ThrowInFoolPlayerMonitorTest {
    AutoCloseable closeable;
    @Mock
    Player player1;
    @Mock
    Player player2;
    @Mock
    Player player3;
    @Mock
    GameFactory gameFactory;
    @Mock
    MessageService messageService;
    @Spy
    @InjectMocks
    ThrowInFoolPlayerMonitor throwInFoolPlayerMonitor;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void addPlayer_whenAddedOnePlayer_thenQueueSizeIsOne() {

        throwInFoolPlayerMonitor.addPlayer("ThrowInFool", player1);

        assertEquals(1, throwInFoolPlayerMonitor.getQueueSize());
    }

    @Test
    void addPlayer_whenAddedTwoPlayer_thenRunGameIfPlayersEqualsIsCalledAndQueueSizeIsZero() {

        throwInFoolPlayerMonitor.addPlayer("ThrowInFool", player1);
        throwInFoolPlayerMonitor.addPlayer("ThrowInFool", player2);

        verify(throwInFoolPlayerMonitor, times(1)).runGameIfPlayersEquals(2, player2, "ThrowInFool");
        assertEquals(0, throwInFoolPlayerMonitor.getQueueSize());
    }

    @Test
    void addPlayer_whenAddedThreePlayer_thenRunGameIfPlayersEqualsIsCalledAndQueueSizeIsOne() {

        throwInFoolPlayerMonitor.addPlayer("ThrowInFool", player1);
        throwInFoolPlayerMonitor.addPlayer("ThrowInFool", player2);
        throwInFoolPlayerMonitor.addPlayer("ThrowInFool", player3);

        verify(throwInFoolPlayerMonitor, times(1)).runGameIfPlayersEquals(2, player2, "ThrowInFool");
        assertEquals(1, throwInFoolPlayerMonitor.getQueueSize());
    }

    @Test
    void runGameIfPlayersEquals_whenQueueSizeIsOne_thenGameFactoryIsNotInvokingAndInvokingSendMessage() {
        throwInFoolPlayerMonitor.addPlayer("ThrowInFool", player1);

        verify(gameFactory, times(0)).runGame(eq("ThrowInFool"), any(List.class));
        verify(messageService, times(1)).sendMessageTo(player1.getChatID(), "Ждем игроков");
    }

    @Test
    void runGameIfPlayersEquals_whenQueueSizeIsTwo_thenGameFactoryIsInvoking() {
        throwInFoolPlayerMonitor.addPlayer("ThrowInFool", player1);
        throwInFoolPlayerMonitor.addPlayer("ThrowInFool", player2);

        verify(gameFactory, times(1)).runGame(eq("ThrowInFool"), any(List.class));
    }

    @Test
    void getPlayers_whenInvoked_thenReturnListIsContainsTwoPlayersFromQueue() {
        throwInFoolPlayerMonitor.addPlayer("ThrowInFool", player1);
        throwInFoolPlayerMonitor.addPlayer("ThrowInFool", player2);
        List<Player> expected = new ArrayList<>();
        expected.add(player1);
        expected.add(player2);

        verify(gameFactory, times(1)).runGame(eq("ThrowInFool"), eq(expected));

    }
}