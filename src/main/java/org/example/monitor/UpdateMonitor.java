package org.example.monitor;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;



public interface UpdateMonitor {
    ConcurrentHashMap<Long, CompletableFuture<Update>> updates = new ConcurrentHashMap<>();

    default void addMessageToUpdateMonitor(Long chatId, Update update) {
        CompletableFuture<Update> future;
        if (updates.containsKey(chatId)) {
            future = updates.get(chatId);
            future.complete(update);
            updates.putIfAbsent(chatId, future);
        } else {
            System.err.println("ERROR: Unexpected message: " + update.getMessage().getText() + " from chatId: " + update.getMessage().getChatId());
        }
    }

    default String getMessage(Long chatId) {
        updates.remove(chatId);
        CompletableFuture<Update> future = new CompletableFuture<>();
        updates.putIfAbsent(chatId, future);
        return future.join().getMessage().getText();
    }
}
