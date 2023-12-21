package org.example.service;


import org.example.model.Player;
import org.example.network.TelegramBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

public interface MessageService {
    default void sendMessageTo(Long chatId, String message, TelegramBot bot) {
        try {
            SendMessage sendMessage = new SendMessage(chatId.toString(), message);
            sendMessage.enableHtml(true);
            bot.execute(sendMessage);
        } catch (Exception e) {
            System.err.println("in MessageService.sendMessageTo(" + chatId + ", " + message + "): " + e.getMessage());
        }
    }

    default void sendMessageTo(Player player, String message, TelegramBot bot) {
        try {
            SendMessage sendMessage = new SendMessage(player.getChatID().toString(), message);
            sendMessage.enableHtml(true);
            bot.execute(sendMessage);
        } catch (Exception e) {
            System.err.println("in MessageService.sendMessageTo(" + player.getChatID() + ", " + message + "): " + e.getMessage());
        }
    }

    default void sendMessageToAll(List<Player> players, String message, TelegramBot bot) {
        players.forEach(player -> sendMessageTo(player, message, bot));
    }

    default String receiveMessageFrom(Player player, TelegramBot bot) {
        return bot.getUpdateMonitor().getMessage(player.getChatID());
    }

    default String receiveMessageFrom(Long chatId, TelegramBot bot) {
        return bot.getUpdateMonitor().getMessage(chatId);
    }
}
