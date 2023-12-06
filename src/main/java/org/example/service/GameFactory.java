package org.example.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.network.TelegramBot;
import org.example.state.ThrowInFool;



@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GameFactory {
    TelegramBot bot;

    public GameFactory(TelegramBot bot) {
        this.bot = bot;
    }

    public void createGame() {
        //fixme DEBUG
        System.out.println("DEBUG: GameFactory starts");
        new Thread(() -> {
            //fixme DEBUG
            System.out.println("DEBUG: GameFactory.createGame()");
            new ThrowInFool(bot).execute();
        }).start();
    }
}
