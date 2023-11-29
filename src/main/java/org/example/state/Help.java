package org.example.state;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.example.network.TelegramBot;
import org.example.service.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Help  implements State {
    TelegramBot bot;
    final String help = "Добро пожаловать!";

    @Autowired
    public Help(TelegramBot bot) {
        this.bot = bot;
    }

    public BotApiMethod<?> perform(Long chatId) {
        return help(bot, chatId);
    }

    private BotApiMethod<?> help(TelegramBot bot, Long chatId) {
        return new SendMessage(chatId.toString(), help);
    }
}
