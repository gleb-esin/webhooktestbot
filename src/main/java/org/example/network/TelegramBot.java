package org.example.network;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.example.service.ClientCommandContext;
import org.example.service.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class TelegramBot extends TelegramWebhookBot {
    String botPath;
    String botUsername;
    String botToken;
    final List<String> commands = Arrays.asList("/start");

    @Autowired
    public TelegramBot(SetWebhook setWebhook) {
        super("${telegrambot.webHookPath}");
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        try {
            handleUpdate(update);
        } catch (IllegalArgumentException e) {
            return new SendMessage(update.getMessage().getChatId().toString(),
                    "IllegalArgumentException is greetings You!");
        } catch (Exception e) {
            return new SendMessage(update.getMessage().getChatId().toString(),
                    "Exception is greetings You!");
        }
        return null;
    }

    @Override
    public String getBotPath() {
        return null;
    }

    private void handleUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            boolean isCommand = commands.stream().anyMatch(text::startsWith);
            if (isCommand) {
                new ClientCommandContext(this).processRequestFrom(chatId, text);
            } else {
                new MessageHandler(this).sendMessageTo(chatId, "Сообщение не распознано");
            }
        }
    }
}
