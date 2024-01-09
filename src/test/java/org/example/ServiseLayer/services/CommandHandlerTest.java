package org.example.ServiseLayer.services;

import org.example.BusinessLayer.states.Help;
import org.example.EntityLayer.Player;
import org.example.ServiseLayer.factories.PlayerBuilder;
import org.example.ServiseLayer.factories.PlayerMonitorFactory;
import org.example.ServiseLayer.monitors.MessageMonitor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CommandHandlerTest {
    AutoCloseable closeable;
    @Mock
    Help help;
    @Mock
    MessageMonitor messageMonitor;
    @Mock
    PlayerBuilder playerBuilder;
    @Mock
    PlayerMonitorFactory playerMonitorFactory;
    @InjectMocks
    CommandHandler commandHandler;
    @Mock
    Player player;

    @BeforeEach
    void setUp() {
        closeable = org.mockito.MockitoAnnotations.openMocks(this);
        player = new Player(1L, "Test Name");
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void handleCommand_whenCommandIsStart_thenHelpIsExecuted() {
        commandHandler.handleCommand(1L, "/start");

        verify(help).execute(1L);
    }

    @Test
    void handleCommand_whenCommandIsHelp_thenHelpIsExecuted() {
        commandHandler.handleCommand(1L, "/help");

        verify(help).execute(1L);
    }

    @Test
    void handleCommand_whenCommandIsThrowinfool_thenPlayerMonitorFactoryIsCalled() throws InterruptedException {
        when(playerBuilder.createPlayer(1L)).thenReturn(player);
        commandHandler.handleCommand(1L, "/throwinfool");
        Thread.sleep(1000);
        verify(playerBuilder).createPlayer(1L);
        verify(playerMonitorFactory).addPlayer(player, "throwinfool");
    }

    @Test
    void handleCommand_whenInputIsNotCommand_thenMessageMonitorIsCalled() {
        commandHandler.handleCommand(1L, "message not recognized as command");

        verify(messageMonitor).completeRequestedMessage(1L, "message not recognized as command");
    }
}