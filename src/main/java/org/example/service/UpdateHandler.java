package org.example.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.model.Player;
import org.example.monitor.MessageMonitor;
import org.example.monitor.PlayerMonitor;
import org.example.state.Help;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UpdateHandler {
    PlayerFactory playerfactory;
    PlayerMonitor playerMonitor;
    MessageMonitor messageMonitor;
    MessageService messageService;

    public void handleUpdate(Update update) {
        Long chatId;
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            chatId = update.getMessage().getChatId();
            switch (text) {
                case "/start", "/help" -> new Help(chatId, messageService).execute();
                case "/throwinfool" -> {
                    ExecutorService executorService = Executors.newSingleThreadExecutor();
                    executorService.submit(() -> {
                        Player player = playerfactory.createPlayer(chatId);
                        playerMonitor.addPlayerToThrowInFoolWaiters(player);
                    });
                    executorService.shutdown();
                }
                default -> messageMonitor.addRequestToIncomingMessages(chatId, update);
            }
        }
    }
}
