package org.example.ServiseLayer.services;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.BusinessLayer.states.Help;
import org.example.EntityLayer.Player;
import org.example.ServiseLayer.factories.PlayerBuilder;
import org.example.ServiseLayer.factories.PlayerMonitorFactory;
import org.example.ServiseLayer.monitors.GameMonitor;
import org.example.ServiseLayer.monitors.MessageMonitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * CommandHandler class handles messages and commands from the user.
 */
@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommandHandler implements ClientCommandHandler {
    PlayerBuilder playerBuilder;
    MessageMonitor messageMonitor;
    Help help;
    PlayerMonitorFactory playerMonitorFactory;
    GameMonitor gameMonitor;
    MessageService messageService;

    /**
     * Handle a messages and commands from the user.
     *
     * @param id   the ID of the user
     * @param text the text to be handled
     */
    @Override
    public void handleCommand(Long id, String text) {
        switch (text) {
            case "/start", "/help" -> help.execute(id);
            case "/throwinfool", "/transferfool" -> {
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.submit(() -> {
                    Player player = playerBuilder.createPlayer(id);
                    if (playerNotInGame(player)) {
                        playerMonitorFactory.addPlayer(player, text.substring(1));
                    }
                });
                executorService.shutdown();
            }
            default -> messageMonitor.completeRequestedMessage(id, text);
        }
    }

    private boolean playerNotInGame(Player player) {
        UUID uuid = gameMonitor.getGameId(player);
        if (uuid != null) {
            if (playerWantsToContinue(player, uuid)) {
                return false;
            }
        }
        return true;
    }

    private boolean playerWantsToContinue(Player player, UUID uuid) {
        String question = "Вы уже участвуете в игре. Хотите покинуть текущую игру?";
        String[] buttons = {"Да--Yes", "Нет--No"};
        messageService.sendInlineKeyboard(player, question, buttons);
        String answer = messageService.receiveMessageFrom(player);
        if (answer.equals("Yes")) {
            messageService.sendMessageTo(player, "Вы покинули текущую игру\nВыберите что-нибудь из меню");
            List<Player> players = gameMonitor.getPlayersList(uuid);
            players.remove(player);
            messageService.sendMessageToAll(players, player.getName() + " покинул игру");
            gameMonitor.removeGame(uuid);
            return false;
        }
        messageService.sendMessageTo(player, "Продолжаем игру");
        return true;
    }
}
