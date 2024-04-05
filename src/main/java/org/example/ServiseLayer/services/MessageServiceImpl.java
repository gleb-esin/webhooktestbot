package org.example.ServiseLayer.services;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.EntityLayer.Player;
import org.example.ServiseLayer.monitors.MessageMonitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;
/**
 * A service implementation class for sending and receiving messages.
 */
@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageServiceImpl implements MessageService {
    MessageMonitor messageMonitor;
    ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void sendMessageTo(Long chatId, String message) {
        publishEvent(new SendMessage(chatId.toString(), message));
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
        return messageMonitor.requestIncomingMessage(player.getChatID());
    }

    @Override
    public String receiveMessageFrom(Long chatId) {
        return messageMonitor.requestIncomingMessage(chatId);
    }

    private void publishEvent(Object event) {
        applicationEventPublisher.publishEvent(event);
    }
}
