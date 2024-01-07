package org.example.ServiseLayer.factories;

import org.example.EntityLayer.Player;
import org.example.EntityLayer.UserEntity;
import org.example.DataLayer.UserEntityRepository;
import org.example.ServiseLayer.services.MessageService;
import org.springframework.stereotype.Component;

/**
 * A builder class for creating instances of the Player class.
 */
@Component
public record PlayerBuilder(UserEntityRepository userEntityRepository, MessageService messageService) {

    /**
     * Creates a new player with the given chat ID.
     *
     * @param  chatId the chat ID of the player
     * @return        the newly created player
     */
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