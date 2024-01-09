package org.example.ServiseLayer.services;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.EntityLayer.Player;
import org.example.ServiseLayer.factories.PlayerBuilder;
import org.example.ServiseLayer.factories.PlayerMonitorFactory;
import org.example.ServiseLayer.monitors.MessageMonitor;
import org.example.BusinessLayer.states.Help;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * CommandHandler class handles messages and commands from the user.
 */
@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommandHandler implements ClientCommandHandler {
    PlayerBuilder playerfactory;
    MessageMonitor messageMonitor;
    Help help;
    PlayerMonitorFactory playerMonitorFactory;

    /**
     * Handle a messages and commands from the user.
     *
     * @param  id       the ID of the user
     * @param  command  the command to be handled
     */
    @Override
    public void handleCommand(Long id, String command) {
        System.err.println("run handleCommand in " + Thread.currentThread().getName());
        switch (command) {
            case "/start", "/help" -> help.execute(id);
            case "/throwinfool", "/transferfool" -> {
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.submit(() -> {
                    Player player = playerfactory.createPlayer(id);
                    playerMonitorFactory.addPlayer(player, command.substring(1));
                });
                executorService.shutdown();
            }
            default -> messageMonitor.completeRequestedMessage(id, command);
        }
    }
}
