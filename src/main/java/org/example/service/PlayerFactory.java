package org.example.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Player;
import org.example.model.UserEntity;
import org.example.network.TelegramBot;
import org.example.network.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.example.service.MessageHandler.*;

@Service
@Slf4j
@Getter
@AllArgsConstructor(onConstructor_ = {@Autowired})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlayerFactory {
    TelegramBot bot;

    public Player createPlayer(Long chatId) {
        System.out.println("PlayerFactory.createPlayer starts for chatId: " + chatId);
        Player player = null;
        UserEntityRepository userEntityRepository = bot.getUserEntityRepository();
        boolean playerIsRegistered = bot.getUserEntityRepository().existsByUserId(chatId);
        System.out.println("playerIsRegistered: " + playerIsRegistered);
        if (!playerIsRegistered) {
            sendMessageTo(chatId, "Для участия в игре нужно зарегистрироваться. Выберите ваше имя в игре: ");
            String name = receiveMessageFrom(chatId);
            System.out.println("name: " + name);
            boolean nameIsTaken = userEntityRepository.existsByName(name);
            while (nameIsTaken) {
                sendMessageTo(chatId, "Такое имя уже занято. Пожалуйста, выберите другое: ");
                name = receiveMessageFrom(chatId);
            }
            UserEntity userEntity = new UserEntity(chatId, name);
            userEntityRepository.saveAndFlush(userEntity);
            player = new Player(chatId, name, bot);
            sendMessageTo(chatId, name + ", Вы успешно зарегистрированы в игре!");
        } else {
            String name = userEntityRepository.findNameByUserId(chatId);
            player = new Player(chatId, name, bot);
            sendMessageTo(chatId, "С возвращением, " + name);
        }
        return player;
    }
}