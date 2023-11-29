package org.example.state;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.model.Player;
import org.example.monitor.PlayerMonitor;
import org.example.network.TelegramBot;
import org.example.service.PlayerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor(onConstructor_ = {@Autowired})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ThrowInFoolState implements State {
    TelegramBot bot;

    public void perform(Long chatId) {
        System.out.println();
        Player player = new PlayerFactory(bot).createPlayer(chatId);
        PlayerMonitor.addThrowInFoolWaiter(player);
    }
}
