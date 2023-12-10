package org.example.service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Player;
import org.example.model.UserEntity;
import org.example.network.DAO;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlayerFactory implements MessageService, DAO {
    Long chatId;
    CompletableFuture<String> futureMessage = new CompletableFuture<>();

    public PlayerFactory(Long chatId) {
        this.chatId = chatId;
    }

    public Player createPlayer() {
        System.out.println("DEBUG: PlayerFactory.createPlayer starts for chatId: " + chatId);
        Player player;
        boolean playerIsNotRegistered = !existsByUserId(chatId);
        System.out.println("DEBUG: PlayerFactory.createPlayer.playerIsRegistered: " + playerIsNotRegistered);
        if (playerIsNotRegistered) {
            sendMessageTo(chatId, "Для участия в игре нужно зарегистрироваться. Выберите ваше имя в игре: ");
            String name = receiveMessageFrom(chatId);
            System.out.println("DEBUG: name: " + name);
            boolean nameIsTaken = existsByName(name);
            while (nameIsTaken) {
                sendMessageTo(chatId, "Такое имя уже занято. Пожалуйста, выберите другое: ");
                name = receiveMessageFrom(chatId);
            }
            UserEntity userEntity = new UserEntity(chatId, name);
            saveInDB(userEntity);
            player = new Player(chatId, name);
            sendMessageTo(chatId, name + ", Вы успешно зарегистрированы в игре!");
        } else {
            player = new Player(findByUserId(chatId));
            sendMessageTo(chatId, "С возвращением, " + player.getName() + "!\n"+
                    player.getStatistics());
        }
        return player;
    }
}