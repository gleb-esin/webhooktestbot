package org.example.service;

import org.example.model.Player;
import org.example.model.UserEntity;
import org.example.network.TelegramBot;

public record PlayerFactory(Long chatId, TelegramBot bot) {

    public Player createPlayer() {
        Player player;
        boolean playerIsNotRegistered = !bot.getUserEntityRepository().existsByUserId(chatId);
        if (playerIsNotRegistered) {
            bot.sendMessageTo(chatId, "Для участия в игре нужно зарегистрироваться. Выберите ваше имя в игре: ");
            String name = bot.receiveMessageFrom(chatId);
            boolean nameIsTaken = bot.getUserEntityRepository().existsByName(name);
            while (nameIsTaken) {
                bot.sendMessageTo(chatId, "Такое имя уже занято. Пожалуйста, выберите другое: ");
                name = bot.receiveMessageFrom(chatId);
            }
            UserEntity userEntity = new UserEntity(chatId, name);
            bot.getUserEntityRepository().save(userEntity);
            player = new Player(chatId, name);
            bot.sendMessageTo(chatId, name + ", Вы успешно зарегистрированы в игре!");
        } else {
            player = new Player(bot.getUserEntityRepository().findByUserId(chatId));
            bot.sendMessageTo(chatId, "С возвращением, " + player.getName() + "!\n" +
                    player.getStatistics());
        }
        return player;
    }
}