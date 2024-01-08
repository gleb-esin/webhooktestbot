package org.example.ServiseLayer.services;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.EntityLayer.Player;
import org.example.ServiseLayer.monitors.MessageMonitor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@FieldDefaults(level = AccessLevel.PRIVATE)
class MessageServiceImplTest {
    AutoCloseable closeable;
    @Mock
    MessageMonitor messageMonitor;
    @Mock
    ApplicationEventPublisher applicationEventPublisher;
    Player player1 = new Player(1L, "test");
    List<Player> players = List.of(player1, new Player(2L, "test2"));
    SendMessage sendMessage = new SendMessage(Long.toString(1L), "message");
    @InjectMocks
    MessageServiceImpl messageService;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void sendMessageTo_chatId_thenPublishEventIsCalled() {
        messageService.sendMessageTo(1L, "message");

        verify(applicationEventPublisher).publishEvent(sendMessage);
    }

    @Test
    void SendMessageTo_player_thenPublishEventIsCalled() {
        messageService.sendMessageTo(player1, "message");

        verify(applicationEventPublisher).publishEvent(sendMessage);

    }

    @Test
    void sendMessageToAll_thenPublishEventIsCalled() {
        messageService.sendMessageToAll(players, "message");

        verify(applicationEventPublisher).publishEvent(sendMessage);
        verify(applicationEventPublisher).publishEvent(new SendMessage(Long.toString(2L), "message"));
    }

    @Test
    void receiveMessageFrom_chatId_thenMessageReturned() {
        when(messageMonitor.requestIncomingMessage(1L)).thenReturn("message");

        String expected = messageService.receiveMessageFrom(1L);

        assertEquals("message", expected);

    }

    @Test
    void receiveMessageFrom_player_thenMessageReturned() {
        when(messageMonitor.requestIncomingMessage(player1.getChatID())).thenReturn("message");

        String expected = messageService.receiveMessageFrom(player1);

        assertEquals("message", expected);

    }
}