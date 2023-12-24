package org.example.monitor;

import org.example.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

class PlayerMonitorTest {
    @Mock
    Player player1 = new Player(1L, "player1");
    @Mock
    Player player2 = new Player(2L, "player2");
    @Mock
    Player player3 = new Player(3L, "player3");
    PlayerMonitor playerMonitor;

    @BeforeEach
    void setUp() {
        playerMonitor = new PlayerMonitor();

    }

    @Test
    void addPlayerToThrowInFoolWaiters_whenAddedOnePlayer_thenThrowInFoolWaiterListSizeIsOne() {
        playerMonitor.addPlayerToThrowInFoolWaiters(player1);

        assertEquals(1, playerMonitor.getThrowInFoolWaiterListSize());
    }

    @Test
    void getThrowInFoolWaiterList_whenAddedTwoPlayers_thenReturnsTwoPlayers() {
        playerMonitor.addPlayerToThrowInFoolWaiters(player1);
        playerMonitor.addPlayerToThrowInFoolWaiters(player2);

        assertEquals(2, playerMonitor.getThrowInFoolWaiterListSize());
    }

    @Test
    void getThrowInFoolWaiterList_whenAddedThreePlayers_thenReturnsThreePlayers() {
        playerMonitor.addPlayerToThrowInFoolWaiters(player1);
        playerMonitor.addPlayerToThrowInFoolWaiters(player2);
        playerMonitor.addPlayerToThrowInFoolWaiters(player3);

        assertEquals(2, playerMonitor.getThrowInFoolWaiterList().size());
    }

    @Test
    void getThrowInFoolWaiterListSize_whenAddedOnePlayer_thenReturnsOnePlayer() {
        playerMonitor.addPlayerToThrowInFoolWaiters(player1);

        assertEquals(1, playerMonitor.getThrowInFoolWaiterListSize());

    }
}