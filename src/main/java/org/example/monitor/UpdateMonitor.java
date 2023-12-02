package org.example.monitor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicReference;

@Component
@Slf4j
public class UpdateMonitor {
    public ConcurrentHashMap<Long, CompletableFuture<Update>> updates = new ConcurrentHashMap<>();

    public void add(Long chatId, Update update) {
        //fixme DEBUG
        System.out.println("DEBUG: UpdateMonitor.add contains: " + updates.containsKey(chatId));
        CompletableFuture<Update> future;
        if (updates.containsKey(chatId)) {
            future = updates.get(chatId);
            System.out.println("DEBUG: UpdateMonitor.add future is null: " + (future == null));
            future.complete(update);
            updates.putIfAbsent(chatId, future);
            //fixme DEBUG
            System.out.println("DEBUG: UpdateMonitor add message: " + update.getMessage().getText() + " from chatId: " + update.getMessage().getChatId());
        } else {
            System.err.println("ERROR: Unexpected message");
        }
    }

    public CompletableFuture<Update> getMessage(Long chatId) {
        updates.remove(chatId);
        //fixme DEBUG
        System.out.println("DEBUG: UpdateMonitor.getMessage starts for chatId: " + chatId);
        CompletableFuture<Update> future = new CompletableFuture<>();
        //fixme DEBUG
        System.out.println("DEBUG: UpdateMonitor.getMessage() put new future for chatId: " + chatId);
        updates.putIfAbsent(chatId, future);
        return future;
    }
}
