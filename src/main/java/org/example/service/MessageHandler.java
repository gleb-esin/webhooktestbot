package org.example.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.network.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Getter
@AllArgsConstructor(onConstructor_ = {@Autowired})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageHandler {
    TelegramBot bot;


    public void sendMessageTo(Long chatId, String notification) {
        SendMessage message = new SendMessage(chatId.toString(), notification);
//        message.setParseMode(ParseMode.HTML);
        try {
            getBot().execute(message);
        } catch (TelegramApiException e) {
            log.error("send(" + chatId + notification + ")" + e.getMessage());
        }
    }
}
