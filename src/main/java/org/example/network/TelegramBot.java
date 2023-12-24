package org.example.network;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.config.Botconfig;
import org.example.model.Player;
import org.example.monitor.GameMonitor;
import org.example.monitor.PlayerMonitor;

import org.example.service.GameFactory;
import org.example.service.PlayerFactory;
import org.example.state.Help;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
@Slf4j
public class TelegramBot extends TelegramWebhookBot  {
    String botPath;
    String botUsername;
    String botToken;
    PlayerMonitor playerMonitor;
    GameFactory gameFactory;
    PlayerFactory playerfactory;
    ConcurrentHashMap<Long, CompletableFuture<Update>> updates = new ConcurrentHashMap<>();

    @Autowired
    public TelegramBot(Botconfig botconfig, PlayerMonitor playerMonitor, GameFactory gameFactory, PlayerFactory playerfactory) {
        super("${telegrambot.botToken}");
        this.botPath = botconfig.getWebHookPath();
        this.botToken = botconfig.getBotToken();
        this.botUsername = botconfig.getUserName();
        this.playerMonitor = playerMonitor;
        this.gameFactory = gameFactory;
        this.playerfactory = playerfactory;

        List<BotCommand> menu = new ArrayList<>();
        menu.add(new BotCommand("/throwinfool", "Подкидной дурак"));
        menu.add(new BotCommand("/help", "Описание бота"));
//        menu.add(new BotCommand("/test", "test game"));
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
                case "/throwinfool" -> {
                   new Thread(() -> {
                        Player player = playerfactory.createPlayer(chatId, this);
                        playerMonitor.addPlayerToThrowInFoolWaiters(player);
                        if (playerMonitor.getThrowInFoolWaiterListSize() == 2) {
                            gameFactory.createThrowInFoolGame(this);
                        } else {
                            sendMessageTo(chatId, "Ждем игроков");
                        }
                    }).start();
                }

                default -> addMessageToUpdateMonitor(chatId, update);
            }
        }
    }

    private void addMessageToUpdateMonitor(Long chatId, Update update) {
        CompletableFuture<Update> future;
        if (updates.containsKey(chatId)) {
            future = updates.get(chatId);
            future.complete(update);
            updates.putIfAbsent(chatId, future);
        }
    }

    private String getMessage(Long chatId) {
        updates.remove(chatId);
        CompletableFuture<Update> future = new CompletableFuture<>();
        updates.putIfAbsent(chatId, future);
        return future.join().getMessage().getText();
    }

    public void sendMessageTo(Long chatId, String message) {
        try {
            SendMessage sendMessage = new SendMessage(chatId.toString(), message);
            sendMessage.enableHtml(true);
            execute(sendMessage);
        } catch (Exception e) {
            System.err.println("in MessageService.sendMessageTo(" + chatId + ", " + message + "): " + e.getMessage());
        }
    }

    public void sendMessageTo(Player player, String message) {
        try {
            SendMessage sendMessage = new SendMessage(player.getChatID().toString(), message);
            sendMessage.enableHtml(true);
            execute(sendMessage);
        } catch (Exception e) {
            System.err.println("in MessageService.sendMessageTo(" + player.getChatID() + ", " + message + "): " + e.getMessage());
        }
    }

    public void sendMessageToAll(List<Player> players, String message) {
        players.forEach(player -> sendMessageTo(player, message));
    }

    public String receiveMessageFrom(Player player) {
        return getMessage(player.getChatID());
    }

    public String receiveMessageFrom(Long chatId) {
        return getMessage(chatId);
    }
}
