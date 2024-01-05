package org.example.interfaceAdapters.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.entities.Player;
import org.example.interfaceAdapters.monitor.MessageMonitor;
import org.example.frameworks.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageService_BotExecute implements MessageService{
    TelegramBot telegramBot;
    MessageMonitor messageMonitor;


    @Override
    public void sendMessageTo(Long chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(message);
        sendMessage.enableHtml(true);
        try {
            telegramBot.execute(sendMessage);
        } catch (TelegramApiException e) {

        }

    }

    @Override
    public void sendMessageTo(Player player, String message) {
        sendMessageTo(player.getChatID(), message);

    }

    @Override
    public void sendMessageToAll(List<Player> players, String message) {
        players.forEach(player -> sendMessageTo(player, message));

    }

    @Override
    public String receiveMessageFrom(Player player) {
        return receiveMessageFrom(player.getChatID());
    }

    @Override
    public String receiveMessageFrom(Long chatId) {
        return messageMonitor.getIncomingMessage(chatId);
    }
}
