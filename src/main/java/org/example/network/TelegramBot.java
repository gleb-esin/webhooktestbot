package org.example.network;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.config.Botconfig;
import org.example.monitor.PlayerMonitor;

import org.example.monitor.Test_PlayerMonitor;
import org.example.monitor.UpdateMonitor;
import org.example.service.MessageService;
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
import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
@Slf4j
public class TelegramBot extends TelegramWebhookBot implements UpdateMonitor, PlayerMonitor, Test_PlayerMonitor, MessageService, DAO {
    String botPath;
    String botUsername;
    String botToken;
    UserEntityRepository userEntityRepository;

    @Autowired
    public TelegramBot(Botconfig botconfig, UserEntityRepository userEntityRepository) {
        super("${telegrambot.botToken}");
        this.botPath = botconfig.getWebHookPath();
        this.botToken = botconfig.getBotToken();
        this.botUsername = botconfig.getUserName();
        this.userEntityRepository = userEntityRepository;
        setTelegramBotInMessageService(this);
        setTelegramBotInDAO(this);

        List<BotCommand> menu = new ArrayList<>();
        menu.add(new BotCommand("/throwinfool", "Подкидной дурак"));
        menu.add(new BotCommand("/help", "Описание бота"));
        menu.add(new BotCommand("/test", "test game"));
        try {
            execute(new SetMyCommands(menu, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            System.err.println("Error setting bot's command list: " + e.getMessage());
        }
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        handleUpdate(update);
        return null;
    }

    private void handleUpdate(Update update) {
        Long chatId;
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            chatId = update.getMessage().getChatId();
            switch (text) {
                case "/start", "/help" -> new Help(this, chatId).execute();
                case "/throwinfool" -> addPlayerToThrowInFoolWaiters(chatId);
                case "/test" -> addPlayerToTest_ThrowInFoolWaiters(chatId);

                default -> addMessageToUpdateMonitor(chatId, update);
            }
        }
    }
}
