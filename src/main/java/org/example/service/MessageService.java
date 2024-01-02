package org.example.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.model.Player;
import org.example.monitor.MessageMonitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageService  {
    MessageMonitor messageMonitor;
    ApplicationEventPublisher applicationEventPublisher;


    public void sendMessageTo(Long chatId, String message) {
        publishEvent(new SendMessage(chatId.toString(), message));
    }

    public void sendMessageTo(Player player, String message) {
       sendMessageTo(player.getChatID(), message);
    }

    public void sendMessageToAll(List<Player> players, String message) {
        players.forEach(player -> sendMessageTo(player, message));
    }

    public String receiveMessageFrom(Player player) {
        return messageMonitor.getIncomingMessage(player.getChatID());
    }

    public String receiveMessageFrom(Long chatId) {
        return messageMonitor.getIncomingMessage(chatId);
    }

    public void publishEvent(Object event) {
        applicationEventPublisher.publishEvent(event);
        System.out.println("DEBUG Event published: " + event);
    }
}
