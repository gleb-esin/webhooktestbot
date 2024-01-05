package org.example.interfaceAdapters.factories;

import org.example.entities.Player;
import org.example.entities.UserEntity;
import org.example.frameworks.UserEntityRepository;
import org.example.interfaceAdapters.service.MessageService;
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