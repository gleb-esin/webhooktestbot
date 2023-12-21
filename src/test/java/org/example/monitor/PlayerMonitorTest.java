package org.example.monitor;

import org.example.model.Player;
import org.example.network.TelegramBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Spy;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;

class PlayerMonitorTest {
    @Mock
    Player player1;
    @Mock
    Player player2;
    @Mock
    Player player3;
    @Spy
    PlayerMonitor playerMonitor;
    @Mock
    TelegramBot bot;

    @BeforeEach
    void setUp() {
        playerMonitor = spy(PlayerMonitor.class);
//        player1 = new Player(1L, "player1");
//        player2 = new Player(2L, "player2");
//        player3 = new Player(3L, "player3");
//
    }

    @Test
    void addPlayerToThrowInFoolWaiters_whenOnePlayer_then() {
        playerMonitor.addPlayerToThrowInFoolWaiters(1L, bot);

    }

    @Test
    void getThrowInFoolWaiterList() {
    }
}