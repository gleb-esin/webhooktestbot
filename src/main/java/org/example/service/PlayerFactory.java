package org.example.service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Player;
import org.example.model.UserEntity;
import org.example.network.TelegramBot;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlayerFactory implements MessageService {
    TelegramBot bot;
    Long cahtId;
    CompletableFuture<String> futureMessage = new CompletableFuture<>();

    public PlayerFactory(TelegramBot bot, Long chatId) {
        this.bot = bot;
        this.cahtId = chatId;
    }

    public Player createPlayer() {
        System.out.println("DEBUG: PlayerFactory.createPlayer starts for cahtId: " + cahtId);
        Player player;
        boolean playerIsNotRegistered = !bot.getUserEntityRepository().existsByUserId(cahtId);
        System.out.println("DEBUG: PlayerFactory.createPlayer.playerIsRegistered: " + playerIsNotRegistered);
        if (playerIsNotRegistered) {
            sendMessageTo(cahtId, "Для участия в игре нужно зарегистрироваться. Выберите ваше имя в игре: ");
            String name = receiveMessageFrom(cahtId);
            System.out.println("DEBUG: name: " + name);
            boolean nameIsTaken = bot.getUserEntityRepository().existsByName(name);
            while (nameIsTaken) {
                sendMessageTo(cahtId, "Такое имя уже занято. Пожалуйста, выберите другое: ");
                name = receiveMessageFrom(cahtId);
            }
            UserEntity userEntity = new UserEntity(cahtId, name);
            bot.getUserEntityRepository().saveAndFlush(userEntity);
            player = new Player(cahtId, name);
            sendMessageTo(cahtId, name + ", Вы успешно зарегистрированы в игре!");
        } else {
            player = new Player(bot.getUserEntityRepository().findByUserId(cahtId));
            sendMessageTo(cahtId, "С возвращением, " + player.getName() + "!\n"+
                    player.getStatistics());
        }
        return player;
    }
}