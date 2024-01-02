package org.example.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.model.Player;
import org.example.monitor.MessageMonitor;
import org.example.monitor.PlayerMonitor;
import org.example.network.TelegramBot;
import org.example.state.Help;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UpdateHandler {
    PlayerFactory playerfactory;
    PlayerMonitor playerMonitor;
    GameFactory gameFactory;
    MessageMonitor messageMonitor;

    public UpdateHandler(PlayerFactory playerfactory, PlayerMonitor playerMonitor, GameFactory gameFactory, MessageMonitor messageMonitor) {
        this.playerfactory = playerfactory;
        this.playerMonitor = playerMonitor;
        this.gameFactory = gameFactory;
        this.messageMonitor = messageMonitor;
    }


    public void handleUpdate(Update update) {
        Long chatId;
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            chatId = update.getMessage().getChatId();
            switch (text) {
                case "/start", "/help" -> new Help(chatId).execute();
                case "/throwinfool" -> {
                    new Thread(() -> {
                        Player player = playerfactory.createPlayer(chatId);
                        playerMonitor.addPlayerToThrowInFoolWaiters(player);
                    }).start();
                }
                default -> messageMonitor.addMessageToIncomingMessages(chatId, update);
            }
        }
    }
}
