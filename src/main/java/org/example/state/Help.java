package org.example.state;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.network.TelegramBot;
import org.example.service.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;

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
        new MessageHandler(bot).sendMessageTo(chatId, help);
    }
}
