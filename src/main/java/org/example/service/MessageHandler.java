package org.example.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Player;
import org.example.monitor.AnswerMonitor;
import org.example.monitor.UpdateMonitor;
import org.example.network.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Slf4j
@Getter
@AllArgsConstructor(onConstructor_ = {@Autowired})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageHandler {

    public static void sendMessageTo(Long chatId, String notification) {
        AnswerMonitor.add(chatId,new SendMessage(chatId.toString(), notification));
    }

    public static String receiveMessageFrom(Long chatId) {
        System.out.println("receiveMessageFrom starts for chatId: " + chatId);
        return UpdateMonitor.getUpdate(chatId).getMessage().getText();
    }

    public static void sendNotificationTo(Player player, String text) {
        sendMessageTo(player.getChatID(), text);
    }

    public static void sendNotificationToAll(List<Player> players, String text) {
        for (Player player : players) {
            sendMessageTo(player.getChatID(), text);
        }
    }
    public static String receiveMessageFrom(Player player) {
        return receiveMessageFrom(player.getChatID());
    }





}
