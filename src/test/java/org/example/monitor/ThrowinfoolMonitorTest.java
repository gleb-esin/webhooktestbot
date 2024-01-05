package org.example.monitor;

import org.example.entities.Player;
import org.example.interfaceAdapters.monitor.ThrowinfoolMonitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ThrowinfoolMonitorTest {
    @Mock
    Player player1 = new Player(1L, "player1");
    @Mock
    Player player2 = new Player(2L, "player2");
    @Mock
    Player player3 = new Player(3L, "player3");
    @Value("${game.maxPlayers}")
    int maxPlayers;
    ThrowinfoolMonitor throwInFoolMonitor;

    @BeforeEach
    void setUp() {
        //FIXME: mockThrowInFoolFactory
//        throwInFoolMonitor = new ThrowInFoolMonitor(mock(ThrowInFoolFactory.class), mock(MessageService_EventListener.class));
        maxPlayers = 2;
        ReflectionTestUtils.setField(throwInFoolMonitor, "maxPlayers", maxPlayers);
    }

    @Test
    void addPlayerToThrowInFoolWaiters_whenAddedOnePlayer_thenThrowInFoolWaiterListSizeIsOne() {
        //FIXME: mockThrowInFoolFactory
//        throwInFoolMonitor.addPlayer(player1);

        assertEquals(1, throwInFoolMonitor.getQueueSize());
    }

    @Test
    void getThrowInFoolWaiterList_whenMaxPlayersEqualsTwoAndAddedTwoPlayers_thenReturnsZeroPlayers() {
//        throwInFoolMonitor.addPlayer(player1);
//        throwInFoolMonitor.addPlayer(player2);

        assertEquals(0, throwInFoolMonitor.getQueueSize());
    }

    @Test
    void getThrowInFoolWaiterList_whenMaxPlayersEqualsTwoAndAddedThreePlayers_thenReturnsOnePlayers() {
        //FIXME: mockThrowInFoolFactory
//        throwInFoolMonitor.addPlayer(player1);
//        throwInFoolMonitor.addPlayer(player2);
//        throwInFoolMonitor.addPlayer(player3);

        assertEquals(1, throwInFoolMonitor.getQueueSize());
    }

    @Test
    void getThrowInFoolWaiterListSize_whenMaxPlayersEqualsTwoAndAddedOnePlayer_thenReturnsOnePlayer() {
        //FIXME: mockThrowInFoolFactory
        //        throwInFoolMonitor.addPlayer(player1);

        assertEquals(1, throwInFoolMonitor.getQueueSize());

    }
}