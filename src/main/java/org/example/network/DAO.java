package org.example.network;

import org.example.model.TelegramBotContainer;
import org.example.model.UserEntity;

public interface DAO {
    TelegramBotContainer telegramBotContainer = new TelegramBotContainer();

    default void setTelegramBotInDAO(TelegramBot bot) {
        telegramBotContainer.setBot(bot);
    }

    default boolean existsByUserId(Long userId) {
        return telegramBotContainer.getBot().getUserEntityRepository().existsByUserId(userId);
    }

    default boolean existsByName(String name) {
        return telegramBotContainer.getBot().getUserEntityRepository().existsByName(name);
    }

    default String findNameByUserId(Long userId) {
        return telegramBotContainer.getBot().getUserEntityRepository().findNameByUserId(userId);
    }

    default UserEntity saveInDB(UserEntity userEntity) {
        return telegramBotContainer.getBot().getUserEntityRepository().saveAndFlush(userEntity);
    }

    default UserEntity findByUserId(Long userId) {
        return telegramBotContainer.getBot().getUserEntityRepository().findByUserId(userId);
    }
}
