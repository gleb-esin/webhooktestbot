package org.example.state;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.example.network.TelegramBot;
import org.example.service.MessageHandler;

import java.util.concurrent.CompletableFuture;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Help implements  MessageHandler {
    TelegramBot bot;
    Long ownerId;
    CompletableFuture<String> futureMessage = new CompletableFuture<>();
    final String help = "Добро пожаловать!";

    public Help(TelegramBot bot, Long ownerId) {
        this.bot = bot;
        this.ownerId = ownerId;
    }

    public void execute() {
        sendMessageTo(ownerId, help);
    }
}
