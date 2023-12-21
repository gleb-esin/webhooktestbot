package org.example.monitor;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class UpdateMonitor {
    ConcurrentHashMap<Long, CompletableFuture<Update>> updates = new ConcurrentHashMap<>();

    public void addMessageToUpdateMonitor(Long chatId, Update update) {
        CompletableFuture<Update> future;
        if (updates.containsKey(chatId)) {
            future = updates.get(chatId);
            future.complete(update);
            updates.putIfAbsent(chatId, future);
        }
    }

    public String getMessage(Long chatId) {
        updates.remove(chatId);
        CompletableFuture<Update> future = new CompletableFuture<>();
        updates.putIfAbsent(chatId, future);
        return future.join().getMessage().getText();
    }
}
