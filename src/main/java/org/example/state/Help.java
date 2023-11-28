package org.example.state;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.network.TelegramBot;
import org.example.service.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@AllArgsConstructor(onConstructor_ = {@Autowired})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Help  implements State {
    TelegramBot bot;
    Long chatId;
    final String help = "Добро пожаловать!";

    @Override
    public void perform() {
        help(bot, chatId);
    }

    private void help(TelegramBot bot, Long chatId) {
        bot.sendMessageTo(chatId, help);
    }
}
