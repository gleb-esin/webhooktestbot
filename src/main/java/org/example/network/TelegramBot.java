package org.example.network;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.config.Botconfig;
import org.example.service.UpdateHandler;
import org.springframework.beans.factory.annotation.Autowired;
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
    UpdateHandler updateHandler;

    @Autowired
    public TelegramBot(Botconfig botconfig, UpdateHandler updateHandler) {
        super("${telegrambot.botToken}");
        this.botPath = botconfig.getWebHookPath();
        this.botToken = botconfig.getBotToken();
        this.botUsername = botconfig.getUserName();
        this.updateHandler = updateHandler;
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
        updateHandler.handleUpdate(update);
        return null;
    }

    @EventListener(SendMessage.class)
    private void eventListener(SendMessage event) {
        try {
            event.enableHtml(true);
            execute(event);
        } catch (TelegramApiException e) {
        }
    }
}
