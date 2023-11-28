package org.example.network;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.config.Botconfig;
import org.example.service.ClientCommandContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Slf4j
public class TelegramBot extends TelegramWebhookBot {
    String botPath;
    String botUsername;
    String botToken;
    final List<String> commands = Arrays.asList("/start");

    @Autowired
    public TelegramBot(Botconfig botconfig) {
        super(new DefaultBotOptions(), botconfig.getBotToken());
        this.botPath = botconfig.getWebHookPath();
        this.botUsername = botconfig.getUserName();
        this.botToken = botconfig.getBotToken();
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

    private void handleUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            boolean isCommand = commands.stream().anyMatch(text::startsWith);
            if (isCommand) {
                new ClientCommandContext(this).processRequestFrom(chatId, text);
            } else {
                sendMessageTo(chatId, "Сообщение не распознано");
            }
        }
    }


    public void sendMessageTo(Long chatId, String notification) {
        SendMessage message = new SendMessage(chatId.toString(), notification);
        message.setParseMode(ParseMode.HTML);
        try {
            this.execute(message);
        } catch (TelegramApiException e) {
            log.error("send(" + chatId + notification + ")" + e.getMessage());
        }
    }
}
