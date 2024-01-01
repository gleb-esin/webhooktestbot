package org.example.network;

import org.example.config.Botconfig;
import org.example.model.Player;
import org.example.monitor.PlayerMonitor;
import org.example.service.GameFactory;
import org.example.service.PlayerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class TelegramBotTest {
    @Mock
    AbsSender absSender;
    @Mock
    SetMyCommands setMyCommandsMock;
    @Mock
    Botconfig botconfig;
    @Mock
    PlayerMonitor playerMonitor;
    @Mock
    GameFactory gameFactory;
    @Mock
    PlayerFactory playerfactory;
    @Mock
    Update update;
    TelegramBot spy;

    @BeforeEach
    public void setUp() throws TelegramApiException {
        MockitoAnnotations.openMocks(this);
        spy = spy(new TelegramBot(botconfig, playerMonitor, gameFactory, playerfactory));

    }

    @Test
    public void onWebhookUpdateReceived()  {
        spy.onWebhookUpdateReceived(update);

        verify(spy).handleUpdate(update);
    }

    @Test
    public void sendMessageToChatId() throws TelegramApiException {
        spy.sendMessageTo(1l, "test");

        verify(spy).execute(any(SendMessage.class));
    }

    @Test
    public void sendMessageToPlayer() throws TelegramApiException {
        Player playerMock = mock(Player.class);
        when(playerMock.getChatID()).thenReturn(1L);
        spy.sendMessageTo(playerMock.getChatID(), "test");

        verify(spy).execute(any(SendMessage.class));
    }

    @Test
    public void sendMessageToAll() throws TelegramApiException {
        Player playerMock1 = mock(Player.class);
        when(playerMock1.getChatID()).thenReturn(1L);
        Player playerMock2 = mock(Player.class);
        when(playerMock2.getChatID()).thenReturn(2L);
        List<Player> playersForNotify = List.of(playerMock1, playerMock2);

        spy.sendMessageToAll(playersForNotify, "test");

        verify(spy, times(2)).execute(any(SendMessage.class));
    }

    @Test
    public void receiveMessageFromChatId() {
        Message message = mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(update.hasMessage()).thenReturn(true);
        when(message.getChatId()).thenReturn(1L);
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn("test");


        new Thread(() -> {
           spy.receiveMessageFrom(1L);
           verify(spy).getMessage(1L);
        });
        new Thread(() -> {
            spy.receiveMessageFrom(1L);
        });



    }

    @Test
    public void testReceiveMessageFromPlayer() {
        Player playerMock = mock(Player.class);
        when(playerMock.getChatID()).thenReturn(1L);
        Message message = mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(update.hasMessage()).thenReturn(true);
        when(message.getChatId()).thenReturn(1L);
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn("test");


        new Thread(() -> {
            String expected = spy.receiveMessageFrom(playerMock);
            assertEquals("test", expected);
        }).start();
        new Thread(() -> {
            spy.receiveMessageFrom(1L);
        });
    }
}