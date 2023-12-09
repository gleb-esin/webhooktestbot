package org.example.network;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.config.Botconfig;
import org.example.monitor.PlayerMonitor;

import org.example.monitor.UpdateMonitor;
import org.example.service.MessageHandler;
import org.example.state.Help;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
@Slf4j
public class TelegramBot extends TelegramWebhookBot implements UpdateMonitor, PlayerMonitor, MessageHandler {
    String botPath;
    String botUsername;
    String botToken;
    UserEntityRepository userEntityRepository;
    final List<String> commands = Arrays.asList("/help");

    @Autowired
    public TelegramBot(Botconfig botconfig, UserEntityRepository userEntityRepository) {
        super("${telegrambot.botToken}");
        this.botPath = botconfig.getWebHookPath();
        this.botToken = botconfig.getBotToken();
        this.botUsername = botconfig.getUserName();
        this.userEntityRepository = userEntityRepository;
        setTelegramBot(this);

        List<BotCommand> menu = new ArrayList<>();
        menu.add(new BotCommand("/throwinfool", "Подкидной дурак"));
        menu.add(new BotCommand("/help", "Описание бота"));
        try {
            execute(new SetMyCommands(menu, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            System.err.println("Error setting bot's command list" + e.getMessage());
        }
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        //fixme DEBUG
        System.out.println("DEBUG: onWebhookUpdateReceived got message: " + update.getMessage().getText() + " from chatId: " + update.getMessage().getChatId());
        handleUpdate(update);
        return null;
    }

    private void handleUpdate(Update update) {
        Long chatId;
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            chatId = update.getMessage().getChatId();

            //fixme DEBUG
            System.out.println("DEBUG: handleUpdate got message from chatId: " + chatId + " with text: " + text);

            switch (text) {
                case "/start", "/help" -> {
                    new Help(this, chatId).execute();
                }
                case "/throwinfool" -> addPlayerToPlayerMonitor(this, chatId);

                default -> addMessageToUpdateMonitor(chatId, update);
            }
        }
    }

}
