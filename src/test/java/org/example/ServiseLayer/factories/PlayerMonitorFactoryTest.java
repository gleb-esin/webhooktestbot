package org.example.ServiseLayer.factories;

import org.example.BusinessLayer.throwInFool.ThrowinfoolPlayerMonitor;
import org.example.EntityLayer.Player;
import org.example.ServiseLayer.monitors.PlayersMonitor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;

class PlayerMonitorFactoryTest {
    AutoCloseable closeable;
    Map<String, PlayersMonitor> monitorMap;
    @Mock
    ThrowinfoolPlayerMonitor throwinfoolMonitor;
    @Mock
    Player player;
    PlayerMonitorFactory playerMonitorFactory;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        monitorMap = new HashMap<>();
        monitorMap.put("throwinfoolPlayerMonitor", throwinfoolMonitor);
        playerMonitorFactory = new PlayerMonitorFactory(monitorMap);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void addPlayer_whenGameTypeIsCorrect_thenInvokesAddPlayer() {
        playerMonitorFactory.addPlayer(player, "throwinfool");

        verify(throwinfoolMonitor).addPlayer("throwinfool", player);
    }

    @Test
    void addPlayer_whenGameTypeIsNotCorrect_thenAddPlayerIsNotInvoked() {
        playerMonitorFactory.addPlayer(player, "noSuchGameType");
    }
}