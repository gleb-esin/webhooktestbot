package org.example.ServiseLayer.services;

import org.example.BusinessLayer.states.Help;
import org.example.DataLayer.UserEntityRepository;
import org.example.EntityLayer.Player;
import org.example.ServiseLayer.factories.PlayerBuilder;
import org.example.ServiseLayer.factories.PlayerMonitorFactory;
import org.example.ServiseLayer.monitors.MessageMonitor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CommandHandlerTest {
    AutoCloseable closeable;
    @Mock
    Help help;
    @Mock
    MessageMonitor messageMonitor;
    @Mock
    MessageService messageService;
    @Mock
    PlayerMonitorFactory playerMonitorFactory;
    @InjectMocks
    CommandHandler commandHandler;
    @Mock
    Player player;
    UserEntityRepository userEntityRepository;

    @BeforeEach
    void setUp() {
        closeable = org.mockito.MockitoAnnotations.openMocks(this);
        player = new Player(1l, "Test Name");
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
        PlayerBuilder playerBuilder1 = new PlayerBuilder(userEntityRepository, messageService);
        final PlayerBuilder spyPlayerBuilder = spy(playerBuilder1);
        doReturn(player).when(spyPlayerBuilder).createPlayer(1L);
        final CommandHandler commandHandler1 = new CommandHandler(spyPlayerBuilder, messageMonitor, help, playerMonitorFactory);
        AtomicInteger passedTests = new AtomicInteger();
        new Thread(() -> {
            commandHandler1.handleCommand(1L, "/throwinfool");
            // Verify the interactions
            verify(spyPlayerBuilder).createPlayer(1L);
            passedTests.incrementAndGet();
            verify(playerMonitorFactory).addPlayer(player, "throwinfool");
            passedTests.incrementAndGet();
        }).start();
        Thread.sleep(100);
        assertEquals(2, passedTests.get());
    }

    @Test
    void handleCommand_whenInputIsNotCommand_thenMessageMonitorIsCalled() {
        commandHandler.handleCommand(1L, "message not recognized as command");

        verify(messageMonitor).completeRequestedMessage(1L, "message not recognized as command");
    }
}