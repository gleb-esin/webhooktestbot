package org.example.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.model.Player;
import org.example.monitor.GameMonitor;
import org.example.monitor.PlayerMonitor;
import org.example.network.UserEntityRepository;
import org.example.state.ThrowInFool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GameFactory {
    GameMonitor gameMonitor;
    UserEntityRepository userEntityRepository;
    MessageService messageService;

    public void createThrowInFoolGame(List<Player> players) {
        UUID gameID = UUID.randomUUID();
        gameMonitor.addThrowInFoolToGameMonitor(gameID, players);
        Thread gameThread = new Thread(() -> {
                new ThrowInFool(messageService, gameID, players).play();
                finnishGame(messageService, players, gameID);
        });
        gameThread.start();
    }

    public void finnishGame(MessageService bot, List<Player> players, UUID gameID) {
        System.err.println("Игра завершена");
        gameMonitor.removeThrowInFoolToGameMonitor(gameID);
        for (Player player : players) {
            userEntityRepository.save(player.toUserEntity());
        }
    }
}
