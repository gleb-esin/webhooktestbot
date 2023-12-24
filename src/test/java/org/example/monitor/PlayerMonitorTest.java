package org.example.monitor;

import org.example.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

class PlayerMonitorTest {
    @Mock
    Player player1 = new Player(1L, "player1");
    @Mock
    Player player2 = new Player(2L, "player2");
    PlayerMonitor playerMonitor;

    @BeforeEach
    void setUp() {
        playerMonitor = new PlayerMonitor();
        playerMonitor.addPlayerToThrowInFoolWaiters(player1);

    }

    @Test
    void addPlayerToThrowInFoolWaiters_whenAddedOnePlayer_thenThrowInFoolWaiterListSizeIsOne() {
        assertEquals(1, playerMonitor.getThrowInFoolWaiterListSize());
    }

    @Test
    void getThrowInFoolWaiterList_whenAddedTwoPlayers_thenReturnsTwoPlayers() {
        playerMonitor.addPlayerToThrowInFoolWaiters(player2);
        assertEquals(2, playerMonitor.getThrowInFoolWaiterListSize());
    }

    @Test
    void getThrowInFoolWaiterListSize_whenAddedTwoPlayers_thenReturnsTwoPlayers() {
        playerMonitor.addPlayerToThrowInFoolWaiters(player2);

        assertEquals(2, playerMonitor.getThrowInFoolWaiterListSize());

    }
}