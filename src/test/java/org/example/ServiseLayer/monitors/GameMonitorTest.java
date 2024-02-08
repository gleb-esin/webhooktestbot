package org.example.ServiseLayer.monitors;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.EntityLayer.Player;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.verify;

@FieldDefaults(level = AccessLevel.PRIVATE)
class GameMonitorTest {
    AutoCloseable closeable;
    @Mock
    MessageService messageService;
    @Mock
    UUID gameId;
    @Mock
    Player attacker;
    @Mock
    Player defender;
    @Mock
    List players;
    @Spy
    @InjectMocks
    GameMonitor gameMonitor;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        players = new ArrayList<>(List.of(attacker, defender));
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void addGameMonitor_andGetPlayers() {
        gameMonitor.addSession(gameId, players);
        assertEquals(players, gameMonitor.getPlayers(gameId));
    }

    @Test
    void removeThrowInFoolToGameMonitor() {
        gameMonitor.addSession(gameId, players);

        gameMonitor.removeGame(gameId);

        assertNotEquals(players, gameMonitor.getPlayers(gameId));
        verify(messageService).sendMessageToAll(players, "Игра  завершена.\n Выберите что-нибудь из меню");
    }

}