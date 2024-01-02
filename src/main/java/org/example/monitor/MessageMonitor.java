package org.example.monitor;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageMonitor {
    ConcurrentHashMap<Long, CompletableFuture<Update>> incomeMessages = new ConcurrentHashMap<>();

    public void addMessageToIncomingMessages(Long chatId, Update update) {
        CompletableFuture<Update> future;
        if (incomeMessages.containsKey(chatId)) {
            future = incomeMessages.get(chatId);
            future.complete(update);
            incomeMessages.putIfAbsent(chatId, future);
        }
    }

    public String getIncomingMessage(Long chatId) {
        incomeMessages.remove(chatId);
        CompletableFuture<Update> future = new CompletableFuture<>();
        incomeMessages.putIfAbsent(chatId, future);
        return future.join().getMessage().getText();
    }
}
