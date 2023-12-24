package org.example.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.model.Player;
import org.example.monitor.GameMonitor;
import org.example.monitor.PlayerMonitor;
import org.example.network.TelegramBot;
import org.example.network.UserEntityRepository;
import org.example.state.ThrowInFool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GameFactory {
    PlayerMonitor playerMonitor;
    GameMonitor gameMonitor;
    UserEntityRepository userEntityRepository;

    public void createThrowInFoolGame(TelegramBot bot) {
        UUID gameID = UUID.randomUUID();
        List<Player> players = playerMonitor.getThrowInFoolWaiterList();
        gameMonitor.addThrowInFoolToGameMonitor(gameID, players);
        Thread gameThread = new Thread(() -> {
                new ThrowInFool(bot, gameID, players).play();
        });
        gameThread.start();

//        try {
//            // Ожидаем завершения игры
//            gameThread.join();
//
//            // Код, который выполнится после завершения игры
//            finnishGame(bot, players, gameID);
//        } catch (InterruptedException e) {
//            // Обработка прерывания, если необходимо
//            e.printStackTrace();
//        }
    }

    public void finnishGame(TelegramBot bot, List<Player> players, UUID gameID) {
        gameMonitor.removeThrowInFoolToGameMonitor(gameID, bot);
        for (Player player : players) {
            userEntityRepository.save(player.toUserEntity());
        }
    }
}
