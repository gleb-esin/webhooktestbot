package org.example.service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Player;
import org.example.model.UserEntity;
import org.example.monitor.UpdateMonitor;
import org.example.network.TelegramBot;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlayerFactory implements MessageHandler, UpdateMonitor {
    TelegramBot bot;
    Long ownerId;
    CompletableFuture<String> futureMessage = new CompletableFuture<>();

    public PlayerFactory(TelegramBot bot, Long chatId) {
        this.bot = bot;
        this.ownerId = chatId;
    }

    public Player createPlayer() {
        System.out.println("DEBUG: PlayerFactory.createPlayer starts for ownerId: " + ownerId);
        Player player;
        boolean playerIsRegistered = bot.getUserEntityRepository().existsByUserId(ownerId);
        System.out.println("DEBUG: PlayerFactory.createPlayer.playerIsRegistered: " + playerIsRegistered);
        if (!playerIsRegistered) {
            sendMessageTo(ownerId, "Для участия в игре нужно зарегистрироваться. Выберите ваше имя в игре: ");
            String name = getMessage(ownerId);
            System.out.println("DEBUG: name: " + name);
            boolean nameIsTaken = bot.getUserEntityRepository().existsByName(name);
            while (nameIsTaken) {
                sendMessageTo(ownerId, "Такое имя уже занято. Пожалуйста, выберите другое: ");
                name = getMessage(ownerId);
            }
            UserEntity userEntity = new UserEntity(ownerId, name);
            bot.getUserEntityRepository().saveAndFlush(userEntity);
            player = new Player(ownerId, name);
            sendMessageTo(ownerId, name + ", Вы успешно зарегистрированы в игре!");
        } else {
            String name = bot.getUserEntityRepository().findNameByUserId(ownerId);
            player = new Player(ownerId, name);
            sendMessageTo(ownerId, "С возвращением, " + name);
        }

        return player;
    }
}