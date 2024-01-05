package org.example.frameworks;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.frameworks.config.Botconfig;
import org.example.interfaceAdapters.service.ClientCommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides proxy for get and send information by Telegram servers
 */
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TelegramBot extends TelegramWebhookBot {
    @Getter
    String botPath;
    @Getter
    String botUsername;
    @Getter
    String botToken;
    ClientCommandHandler clientCommandHandler;
    @Autowired
    public TelegramBot(Botconfig botconfig, ClientCommandHandler clientCommandHandler) {
        super("${telegrambot.botToken}");
        this.botPath = botconfig.getWebHookPath();
        this.botToken = botconfig.getBotToken();
        this.botUsername = botconfig.getUserName();
        this.clientCommandHandler = clientCommandHandler;
    }

    @EventListener(ApplicationReadyEvent.class)
    private void registerMenu() {
        try {
            List<BotCommand> menu = new ArrayList<>(
                    List.of(
                            new BotCommand("/throwinfool", "Подкидной дурак"),
                            new BotCommand("/help", "Описание бота")
//                    ,new BotCommand("/test", "test game")
                    )
            );
            execute(new SetMyCommands(menu, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            System.err.println("Error setting bot's command list: " + e.getMessage());
        }
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            clientCommandHandler.handleCommand(chatId, text);
        }
        return null;
    }

    @EventListener(BotApiMethod.class)
    private void eventListener(SendMessage event) {
        try {
            event.enableHtml(true);
            execute(event);
        } catch (TelegramApiException e) {
        }
    }
}
