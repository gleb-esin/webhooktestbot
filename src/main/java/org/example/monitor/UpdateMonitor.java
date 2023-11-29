package org.example.monitor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class UpdateMonitor {
    public static ConcurrentHashMap<Long, CompletableFuture<Update>> updateFutures = new ConcurrentHashMap<>();

    public static void add(Long chatId, Update update) {
        CompletableFuture<Update> future = updateFutures.get(chatId);
        if (future != null) {
            future.complete(update); // Помечаем CompletableFuture как завершенный с обновлением
            System.out.println("UpdateMonitor complete existing future for chatId: " + chatId);
        } else {
            updateFutures.put(chatId, new CompletableFuture<>());
            updateFutures.get(chatId).complete(update);
            System.out.println("UpdateMonitor add new future for chatId: " + chatId);

        }
    }

    public static Update getUpdate(Long chatId) {
        System.out.println("UpdateMonitor.getUpdate starts for chatId: " + chatId);
        CompletableFuture<Update> inCompleteFuture = updateFutures.get(chatId);
        CompletableFuture<Update> completeFuture;

        if (inCompleteFuture == null) {
            inCompleteFuture = new CompletableFuture<>();
            updateFutures.put(chatId, inCompleteFuture);
            System.out.println("...create and put a new one CompletableFuture...");
            try {
                System.out.println("...and trying to return it");
                return inCompleteFuture.get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                log.error("in UpdateMonitor.getUpdate(): " + e.getMessage());
            }
        } else {
            completeFuture = updateFutures.get(chatId);
            System.out.println("UpdateMonitor get a completed CompletableFuture...");
            try {
                System.out.println("...and trying to return it");
                return completeFuture.get();
            } catch (InterruptedException e) {
                log.error("in UpdateMonitor.getUpdate(): " + e.getMessage());
            } catch (ExecutionException e) {
            }
        } return null;
    }
}
