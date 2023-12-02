package org.example.network;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.config.Botconfig;
import org.example.model.Player;
import org.example.monitor.PlayerMonitor;
import org.example.monitor.UpdateMonitor;
import org.example.service.PlayerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
@Slf4j
public class TelegramBot extends TelegramWebhookBot {
    String botPath;
    String botUsername;
    String botToken;
    UserEntityRepository userEntityRepository;
    UpdateMonitor updateMonitor;
    PlayerMonitor playerMonitor;
    final List<String> commands = Arrays.asList("/start", "/newThrowInFool");

    @Autowired
    public TelegramBot(Botconfig botconfig, UserEntityRepository userEntityRepository, UpdateMonitor updateMonitor, PlayerMonitor playerMonitor) {
        super(new DefaultBotOptions(), botconfig.getBotToken());
        this.botPath = botconfig.getWebHookPath();
        this.botUsername = botconfig.getUserName();
        this.botToken = botconfig.getBotToken();
        this.userEntityRepository = userEntityRepository;
        this.updateMonitor = updateMonitor;
        this.playerMonitor = playerMonitor;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        //fixme DEBUG
        System.out.println("DEBUG: onWebhookUpdateReceived got message: " + update.getMessage().getText() + " from chatId: " + update.getMessage().getChatId());
        return handleUpdate(update);
    }

    private BotApiMethod<?> handleUpdate(Update update) {
        Long chatId;
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            chatId = update.getMessage().getChatId();

            //fixme DEBUG
            System.out.println("DEBUG: handleUpdate got message from chatId: " + chatId + " with text: " + text);

            switch (text) {
                case "/start" -> {
                    return new SendMessage(chatId.toString(), "Добро пожаловать!");
                }
                case "/newThrowInFool" -> {
                   new Thread(() -> {
                        playerMonitor.addThrowInFoolWaiter(new PlayerFactory(this).createPlayer(chatId));
                    }, "ThrowInFoolThread").start();
                    return null;
                }
                default -> {
                    updateMonitor.add(chatId, update);
                    return null;
                }
            }
        }
        return null;
    }

    public void sendMessageTo(Long chatId, String notification) {
        try {
            this.execute(new SendMessage(chatId.toString(), notification));
        } catch (Exception e) {
            log.error("in MessageHandler.sendMessageTo(" + chatId + ", " + notification + "): " + e.getMessage());
        }
    }

    public String receiveMessageFrom(Long chatId) {
        //fixme DEBUG
        System.out.println("DEBUG: receiveMessageFrom() starts for chatId: " + chatId);
        return updateMonitor.getMessage(chatId).join().getMessage().getText();
    }

    public void sendNotificationTo(Player player, String text) {
        sendMessageTo(player.getChatID(), text);
    }

    public void sendNotificationToAll(List<Player> players, String text) {
        for (Player player : players) {
            sendMessageTo(player.getChatID(), text);
        }
    }

    public String receiveMessageFrom(Player player) {
        return receiveMessageFrom(player.getChatID());
    }
}
