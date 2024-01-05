package org.example.interfaceAdapters.monitor;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageMonitor {
    ConcurrentHashMap<Long, CompletableFuture<String>> incomeMessages = new ConcurrentHashMap<>();

    public void addRequestToIncomingMessages(Long chatId, String text) {
        CompletableFuture<String> future;
        if (incomeMessages.containsKey(chatId)) {
            future = incomeMessages.get(chatId);
            future.complete(text);
            incomeMessages.putIfAbsent(chatId, future);
        }
    }

    public String getIncomingMessage(Long chatId) {
        incomeMessages.remove(chatId);
        CompletableFuture<String> future = new CompletableFuture<>();
        incomeMessages.putIfAbsent(chatId, future);
        return future.join();
    }
}
