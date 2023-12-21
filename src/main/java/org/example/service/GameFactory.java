package org.example.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.network.TelegramBot;
import org.example.state.ThrowInFool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GameFactory {
    TelegramBot bot;


    public void createThrowInFoolGame() {
        new Thread(() -> new ThrowInFool(bot).play()).start();
    }
}
