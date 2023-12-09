package org.example.service;

import org.example.model.Player;
import org.example.model.TelegramBotContainer;
import org.example.network.TelegramBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

public interface MessageHandler {
    TelegramBotContainer telegramBotContainer = new TelegramBotContainer();


    default void sendMessageTo(Long chatId, String message) {
        try {
            SendMessage sendMessage = new SendMessage(chatId.toString(), message);
            sendMessage.enableHtml(true);
            telegramBotContainer.getBot().execute(sendMessage);
        } catch (Exception e) {
            System.err.println("in MessageHandler.sendMessageTo(" + chatId + ", " + message + "): " + e.getMessage());
        }
    }

    default void sendMessageTo(Player player, String message) {
        try {
            SendMessage sendMessage = new SendMessage(player.getChatID().toString(), message);
            sendMessage.enableHtml(true);
            telegramBotContainer.getBot().execute(sendMessage);
        } catch (Exception e) {
            System.err.println("in MessageHandler.sendMessageTo(" + player.getChatID() + ", " + message + "): " + e.getMessage());
        }
    }

    default void sendMessageToAll(List<Player> players, String message) {
        players.forEach(player -> sendMessageTo(player, message));
    }

    default void setTelegramBot (TelegramBot bot) {
        telegramBotContainer.setBot(bot);
    }
}
