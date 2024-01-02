package org.example.service;

import org.example.model.Player;
import org.example.model.UserEntity;
import org.example.network.TelegramBot;
import org.example.network.UserEntityRepository;
import org.springframework.stereotype.Component;

@Component
public record PlayerFactory(UserEntityRepository userEntityRepository, MessageService messageService) {

    public Player createPlayer(Long chatId) {
        Player player;
        boolean playerIsNotRegistered = !userEntityRepository.existsByUserId(chatId);
        if (playerIsNotRegistered) {
            messageService.sendMessageTo(chatId, "Для участия в игре нужно зарегистрироваться. Выберите ваше имя в игре: ");
            String name = messageService.receiveMessageFrom(chatId);
            boolean nameIsTaken = userEntityRepository.existsByName(name);
            while (nameIsTaken) {
                messageService.sendMessageTo(chatId, "Такое имя уже занято. Пожалуйста, выберите другое: ");
                name = messageService.receiveMessageFrom(chatId);
            }
            UserEntity userEntity = new UserEntity(chatId, name);
            userEntityRepository.save(userEntity);
            player = new Player(chatId, name);
            messageService.sendMessageTo(chatId, name + ", Вы успешно зарегистрированы в игре!");
        } else {
            player = new Player(userEntityRepository.findByUserId(chatId));
            messageService.sendMessageTo(chatId, "С возвращением, " + player.getName() + "!\n" +
                    player.getStatistics());
        }
        return player;
    }
}