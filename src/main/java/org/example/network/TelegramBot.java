package org.example.network;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.config.Botconfig;
import org.example.monitor.AnswerMonitor;
import org.example.monitor.UpdateMonitor;
import org.example.service.ClientCommandContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

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
    UserEntityRepository userEntityRepository;
    final List<String> commands = Arrays.asList("/start", "/newThrowInFool");

    @Autowired
    public TelegramBot(Botconfig botconfig, UserEntityRepository userEntityRepository) {
        super(new DefaultBotOptions(), botconfig.getBotToken());
        this.botPath = botconfig.getWebHookPath();
        this.botUsername = botconfig.getUserName();
        this.botToken = botconfig.getBotToken();
        this.userEntityRepository = userEntityRepository;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        System.out.println("onWebhookUpdateReceived get message: " + update.getMessage().getText() + " from chatId: " + update.getMessage().getChatId());
        handleUpdate(update);
        return null;
    }

    private BotApiMethod<?> handleUpdate(Update update) {
        Long chatId = null;
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            chatId = update.getMessage().getChatId();

            boolean isCommand = commands.contains(text);
            System.out.println(text + "isCommands: " + isCommand);
            if (isCommand) {
                new ClientCommandContext(this).processRequestFrom(chatId, text);
            } else {
                UpdateMonitor.add(chatId, update);
                System.out.println("Update placed in to UpdateMonitor");
            }
        }
        return AnswerMonitor.get(chatId);
    }
}
