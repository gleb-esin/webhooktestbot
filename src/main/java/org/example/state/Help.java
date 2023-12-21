package org.example.state;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.example.network.TelegramBot;

import java.util.concurrent.CompletableFuture;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Help {
    TelegramBot bot;
    Long chatId;
    CompletableFuture<String> futureMessage = new CompletableFuture<>();
    final String help = "Добро пожаловать!";

    public Help(TelegramBot bot, Long chatId) {
        this.bot = bot;
        this.chatId = chatId;
    }

    public void execute() {
        bot.sendMessageTo(chatId, help);
    }
}
