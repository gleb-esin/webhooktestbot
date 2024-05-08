package org.example.ServiseLayer.monitors;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class provides asynchronous methods to add and retrieve incoming messages from a messages monitor.
 */
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageMonitor {
    ConcurrentHashMap<Long, LinkedList<CompletableFuture<String>>> messagesMonitor = new ConcurrentHashMap<>();

    /**
     * Completes the future with given text from given chatId request.
     *
     * @param chatId the ID of the chat
     * @param text   the text of the incoming message
     */

    public void completeRequestedMessage(Long chatId, String text) {
        CompletableFuture<String> future;
        if (messagesMonitor.containsKey(chatId)) {
            LinkedList<CompletableFuture<String>> deque = messagesMonitor.get(chatId);
            future = deque.peek();
            future.complete(text);

        }
    }

    /**
     * Removes previous not requested message from the monitor.
     * Then creates a request with a new CompletableFuture<String> and adds it to the messagesMonitor map.
     * When the future will be completed, it returns the string with which the CompletableFuture was completed.
     *
     * @param chatId the client ID
     * @return the string with which the CompletableFuture was completed.
     */

    public String requestIncomingMessage(Long chatId) {
        CompletableFuture<String> future = new CompletableFuture<>();
        LinkedList<CompletableFuture<String>> deque;
        if (messagesMonitor.containsKey(chatId)) {
            deque = messagesMonitor.get(chatId);
        } else {
            deque = new LinkedList<>();
        }
        deque.addFirst(future);
        messagesMonitor.put(chatId, deque);
        String text = future.join();
        messagesMonitor.get(chatId).remove(chatId);
        return text;
    }
}
