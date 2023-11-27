package org.example.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.network.TelegramBot;
import org.example.state.Help;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@AllArgsConstructor(onConstructor_ = {@Autowired})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientCommandContext {
    TelegramBot bot;

    public  void processRequestFrom(Long chatId, String text) {
        switch (text) {
            case "/start" -> new Help(bot,chatId).perform();
        }
    }

}
