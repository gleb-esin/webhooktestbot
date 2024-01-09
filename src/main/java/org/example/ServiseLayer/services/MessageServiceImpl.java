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

    /**
     * Sends a message to the specified chat ID.
     *
     * @param  chatId   the ID of the chat to send the message to
     * @param  message  the message to send
     */
    @Override
    public void sendMessageTo(Long chatId, String message) {
        publishEvent(new SendMessage(chatId.toString(), message));
    }

    /**
     * Sends a message to the specified player.
     *
     * @param  player   the player to send the message to
     * @param  message  the message to be sent
     */
    @Override
    public void sendMessageTo(Player player, String message) {
       sendMessageTo(player.getChatID(), message);
    }

    /**
     * Sends a message to list of players.
     *
     * @param  players   the list of players to send the message to
     * @param  message   the message to send
     */
    @Override
    public void sendMessageToAll(List<Player> players, String message) {
        players.forEach(player -> sendMessageTo(player, message));
    }

    /**
     * Receives a message from the specified player.
     *
     * @param  player  the player who sent the message
     * @return         the received message as a string
     */
    @Override
    public String receiveMessageFrom(Player player) {
        return messageMonitor.requestIncomingMessage(player.getChatID());
    }
    /**
     * Retrieves a message from the specified chat ID.
     *
     * @param  chatId  the ID of the chat to retrieve the message from
     * @return         the received message as a string
     */
    @Override
    public String receiveMessageFrom(Long chatId) {
        return messageMonitor.requestIncomingMessage(chatId);
    }

    /**
     * Publishes an event using the given object as the event.
     *
     * @param  event  the event object to be published expected to be an SendMessage object
     * @return        void
     */
    private void publishEvent(Object event) {
        applicationEventPublisher.publishEvent(event);
    }
}
