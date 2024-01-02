package org.example.monitor;

import org.example.model.Player;
import org.example.service.GameFactory;
import org.example.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class PlayerMonitorTest {
    @Mock
    Player player1 = new Player(1L, "player1");
    @Mock
    Player player2 = new Player(2L, "player2");
    @Mock
    Player player3 = new Player(3L, "player3");
    @Value("${game.maxPlayers}")
    int maxPlayers;
    PlayerMonitor playerMonitor;

    @BeforeEach
    void setUp() {
        playerMonitor = new PlayerMonitor(mock(GameFactory.class), mock(MessageService.class));
        maxPlayers = 2;
        ReflectionTestUtils.setField(playerMonitor, "maxPlayers", maxPlayers);
    }

    @Test
    void addPlayerToThrowInFoolWaiters_whenAddedOnePlayer_thenThrowInFoolWaiterListSizeIsOne() {
        playerMonitor.addPlayerToThrowInFoolWaiters(player1);

        assertEquals(1, playerMonitor.getThrowInFoolWaiterSize());
    }

    @Test
    void getThrowInFoolWaiterList_whenMaxPlayersEqualsTwoAndAddedTwoPlayers_thenReturnsZeroPlayers() {
        playerMonitor.addPlayerToThrowInFoolWaiters(player1);
        playerMonitor.addPlayerToThrowInFoolWaiters(player2);

        assertEquals(0, playerMonitor.getThrowInFoolWaiterSize());
    }

    @Test
    void getThrowInFoolWaiterList_whenMaxPlayersEqualsTwoAndAddedThreePlayers_thenReturnsOnePlayers() {
        playerMonitor.addPlayerToThrowInFoolWaiters(player1);
        playerMonitor.addPlayerToThrowInFoolWaiters(player2);
        playerMonitor.addPlayerToThrowInFoolWaiters(player3);

        assertEquals(1, playerMonitor.getThrowInFoolWaiterSize());
    }

    @Test
    void getThrowInFoolWaiterListSize_whenMaxPlayersEqualsTwoAndAddedOnePlayer_thenReturnsOnePlayer() {
        playerMonitor.addPlayerToThrowInFoolWaiters(player1);

        assertEquals(1, playerMonitor.getThrowInFoolWaiterSize());

    }
}