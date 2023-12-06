package org.example.service;

import org.example.model.Player;
import org.example.network.TelegramBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

public interface MessageHandler {

    default void sendMessageTo(TelegramBot bot, Long chatId, String message) {
        try {
            SendMessage sendMessage = new SendMessage(chatId.toString(), "<code>" + message + "</code>");
            sendMessage.enableHtml(true);
            bot.execute(sendMessage);
        } catch (Exception e) {
            System.err.println("in MessageHandler.sendMessageTo(" + chatId + ", " + message + "): " + e.getMessage());
        }
    }

    default void sendMessageTo(TelegramBot bot, Player player, String message) {
        try {
            SendMessage sendMessage = new SendMessage(player.getChatID().toString(), "<code>" + message + "</code>");
            sendMessage.enableHtml(true);
            bot.execute(sendMessage);
        } catch (Exception e) {
            System.err.println("in MessageHandler.sendMessageTo(" + player.getChatID() + ", " + message + "): " + e.getMessage());
        }
    }

    default void sendMessageToAll(TelegramBot bot, List<Player> players, String message) {
        players.forEach(player -> sendMessageTo(bot, player, message));
    }
}
