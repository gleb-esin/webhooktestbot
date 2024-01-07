package org.example.ServiseLayer.services;

import org.example.EntityLayer.Player;

import java.util.List;

public interface MessageService {
    void sendMessageTo(Long chatId, String message);

    void sendMessageTo(Player player, String message);

    void sendMessageToAll(List<Player> players, String message);

    String receiveMessageFrom(Player player);

    String receiveMessageFrom(Long chatId);
}
