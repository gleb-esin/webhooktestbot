package org.example.monitor;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.model.Player;
import org.example.network.TelegramBot;
import org.example.service.MessageService_EventListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;

@FieldDefaults(level = AccessLevel.PRIVATE)
class GameMonitorTest {

    GameMonitor gameMonitor;
    UUID gameId;
    Player attacker;
    Player defender;
    List players;

    @BeforeEach
    void setUp() {
        gameMonitor = new GameMonitor(mock(MessageService_EventListener.class));
        gameId = UUID.fromString("4cfb0e57-13a4-4107-ad6d-67759e912d6d");
        attacker = new Player(1L, "attacker");
        defender = new Player(2L, "defender");
        players = List.of(attacker, defender);
    }

    @Test
    void addThrowInFoolToGameMonitor_andGetPlayers() {
        gameMonitor.addThrowInFoolToGameMonitor(gameId, players);
        assertEquals(players, gameMonitor.getPlayers(gameId));
    }

    @Test
    void removeThrowInFoolToGameMonitor() {
        TelegramBot botMock = Mockito.mock(TelegramBot.class);
        gameMonitor.addThrowInFoolToGameMonitor(gameId, players);

        gameMonitor.removeThrowInFoolToGameMonitor(gameId);

        assertNotEquals(players, gameMonitor.getPlayers(gameId));
//        verify(botMock).sendMessageToAll(players, "Игра  завершена.\n Выберите что-нибудь из меню");
    }

}