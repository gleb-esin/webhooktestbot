package org.example.state;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.example.network.TelegramBot;
import org.example.service.MessageService;

import javax.crypto.MacSpi;
import java.util.concurrent.CompletableFuture;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Help {
    Long chatId;
    MessageService messageService;
    final String help = "Добро пожаловать!";

    public Help(Long chatId) {
        this.chatId = chatId;
    }

    public void execute() {
        messageService.sendMessageTo(chatId, help);
    }
}
