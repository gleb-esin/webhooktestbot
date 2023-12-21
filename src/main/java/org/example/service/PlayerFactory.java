package org.example.service;

import org.example.model.Player;
import org.example.model.UserEntity;
import org.example.network.DAO;
import org.example.network.TelegramBot;

public record PlayerFactory(Long chatId, TelegramBot bot) implements MessageService, DAO {

    public Player createPlayer() {
        Player player;
        boolean playerIsNotRegistered = !existsByUserId(chatId);
        if (playerIsNotRegistered) {
            sendMessageTo(chatId, "Для участия в игре нужно зарегистрироваться. Выберите ваше имя в игре: ", bot);
            String name = receiveMessageFrom(chatId, bot);
            boolean nameIsTaken = existsByName(name);
            while (nameIsTaken) {
                sendMessageTo(chatId, "Такое имя уже занято. Пожалуйста, выберите другое: ", bot);
                name = receiveMessageFrom(chatId, bot);
            }
            UserEntity userEntity = new UserEntity(chatId, name);
            saveInDB(userEntity);
            player = new Player(chatId, name);
            sendMessageTo(chatId, name + ", Вы успешно зарегистрированы в игре!", bot);
        } else {
            player = new Player(findByUserId(chatId));
            sendMessageTo(chatId, "С возвращением, " + player.getName() + "!\n" +
                    player.getStatistics(), bot);
        }
        return player;
    }
}