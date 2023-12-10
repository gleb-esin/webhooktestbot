package org.example.monitor;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;



public interface UpdateMonitor {
    ConcurrentHashMap<Long, CompletableFuture<Update>> updates = new ConcurrentHashMap<>();


    default void addMessageToUpdateMonitor(Long chatId, Update update) {
        //fixme DEBUG
        System.out.println("DEBUG: UpdateMonitor contains " + chatId + ": " + updates.containsKey(chatId));
        CompletableFuture<Update> future;
        if (updates.containsKey(chatId)) {
            future = updates.get(chatId);
            System.out.println("DEBUG: UpdateMonitor.addMessageToUpdateMonitor future is null: " + (future == null));
            future.complete(update);
            updates.putIfAbsent(chatId, future);
            //fixme DEBUG
            System.out.println("DEBUG: UpdateMonitor addMessageToUpdateMonitor message: " + update.getMessage().getText() + " from chatId: " + update.getMessage().getChatId());
        } else {
            System.err.println("ERROR: Unexpected message: " + update.getMessage().getText() + " from chatId: " + update.getMessage().getChatId());
        }
    }

    default String getMessage(Long chatId) {
        updates.remove(chatId);
        //fixme DEBUG
        System.out.println("DEBUG: UpdateMonitor.getMessage starts for chatId: " + chatId);
        CompletableFuture<Update> future = new CompletableFuture<>();
        //fixme DEBUG
        System.out.println("DEBUG: UpdateMonitor.getMessage() put new future for chatId: " + chatId);
        updates.putIfAbsent(chatId, future);
        return future.join().getMessage().getText();
    }
}
