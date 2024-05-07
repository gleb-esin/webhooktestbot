package org.example.DataLayer;


import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.DataLayer.config.BotConfig;
import org.example.ServiseLayer.services.ClientCommandHandler;
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
 * This class provides proxy for get and send information to Telegram servers
 */
@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TelegramBot extends TelegramWebhookBot {
    BotConfig config;
    ClientCommandHandler clientCommandHandler;

    @Autowired
    public TelegramBot(BotConfig config, ClientCommandHandler clientCommandHandler) {
        super(config.getBotToken());
        this.config = config;
        this.clientCommandHandler = clientCommandHandler;
    }

    @EventListener(ApplicationReadyEvent.class)
    private void registerMenu() {
        List<BotCommand> menu = new ArrayList<>();
        menu.add(new BotCommand("/throwinfool", "Подкидной дурак"));
        menu.add(new BotCommand("/transferfool", "Переводной дурак"));
        menu.add(new BotCommand("/help", "Описание бота"));
        try {
            execute(new SetMyCommands(menu, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot's command list" + e.getMessage());
        }
    }

    @Override
    public String getBotPath() {
        return config.getWebHookPath();
    }

    @Override
    public String getBotUsername() {
        return config.getUserName();
    }


    @Override
    public void onRegister() {
    }

    /**
     * A listener method for handling outgoing messages to the server.
     *
     * @param sendMessage the event to be handled, expects a SendMessage object
     */
    @EventListener(SendMessage.class)
    private void messageSenderOnEventListener(SendMessage sendMessage) {
        try {
            sendMessage.enableHtml(true);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Handles the received webhook update. Returns null for a successful answer to server
     *
     * @param update the update received from the webhook
     * @return the BotApiMethod object representing the response to the webhook update
     */
    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            clientCommandHandler.handleCommand(chatId, text);
        }
        return null;
    }
}
