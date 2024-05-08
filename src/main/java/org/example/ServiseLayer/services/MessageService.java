package org.example.ServiseLayer.services;

import org.example.EntityLayer.Player;

import java.util.List;
/**Represents interface for message exchange with user and server
 * */

public interface MessageService {

    /**
     * Sends a message to the specified chat ID.
     *
     * @param  chatId   the ID of the chat to send the message to
     * @param  message  the message to send
     */
    void sendMessageTo(Long chatId, String message);

    /**
     * Sends a message to the specified player.
     *
     * @param  player   the player to send the message to
     * @param  message  the message to be sent
     */
    void sendMessageTo(Player player, String message);

    /**
     * Sends a message to list of players.
     *
     * @param  players   the list of players to send the message to
     * @param  message   the message to send
     */
    void sendMessageToAll(List<Player> players, String message);

    /**
     * Receives a message from the specified player.
     *
     * @param  player  the player who sent the message
     * @return         the received message as a string
     */
    String receiveMessageFrom(Player player);

    /**
     * Retrieves a message from the specified chat ID.
     *
     * @param  chatId  the ID of the chat to retrieve the message from
     * @return         the received message as a string
     */
    String receiveMessageFrom(Long chatId);

    void sendInlineKeyboard(Player player, String question, String[] buttons);
}
