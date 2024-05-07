package org.example.DataLayer;

import org.example.DataLayer.config.BotConfig;
import org.example.ServiseLayer.services.ClientCommandHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.mockito.Mockito.*;

class TelegramBotTest {
    AutoCloseable closeable;
    @Mock
    BotConfig botconfig;
    @Mock
    ClientCommandHandler clientCommandHandler;
    @InjectMocks
    TelegramBot telegramBot;
    @Mock
    Update update;
    @Mock
    Message message;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        TelegramBot telegramBotMock = new TelegramBot(new BotConfig(), clientCommandHandler);
        telegramBot = spy(telegramBotMock);
        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn("test");
        when(message.getChatId()).thenReturn(1L);

    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void onWebhookUpdateReceived_whenUpdateHasMessageAndMessageHasText_thenClientCommandHandlerInvoke() {
        telegramBot.onWebhookUpdateReceived(update);

        verify(clientCommandHandler, times(1)).handleCommand(1L, "test");
    }

    @Test
    void onWebhookUpdateReceived_whenUpdateHasMessageAndMessageHasNotText_thenClientCommandHandlerNotInvoke() {
        when(message.hasText()).thenReturn(false);

        telegramBot.onWebhookUpdateReceived(update);

        verify(clientCommandHandler, never()).handleCommand(1L, "test");
    }

    @Test
    void onWebhookUpdateReceived_whenUpdateHasNotMessage_thenClientCommandHandlerNotInvoke() {
        when(update.hasMessage()).thenReturn(false);

        telegramBot.onWebhookUpdateReceived(update);

        verify(clientCommandHandler, never()).handleCommand(1L, "test");
    }

    @Test
    void messageSenderOnEventListener_whenSendMessage_thenClientCommandHandlerInvoke() {
    }
}