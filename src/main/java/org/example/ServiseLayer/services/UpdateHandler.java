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

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UpdateHandler implements ClientCommandHandler {
    PlayerBuilder playerfactory;
    MessageMonitor messageMonitor;
    MessageService messageService;
    PlayerMonitorFactory playerMonitorFactory;


    @Override
    public void handleCommand(Long id, String command) {
        switch (command) {
            case "/start", "/help" -> new Help(messageService).execute(id);
            case "/throwinfool", "/transferfool" -> {
                try (ExecutorService executorService = Executors.newCachedThreadPool()) {
                    executorService.submit(() -> {
                        Player player = playerfactory.createPlayer(id);
                        playerMonitorFactory.addPlayer(player, command.substring(1));
                    });
                    executorService.shutdown();
                }
            }
            default -> messageMonitor.addRequestToIncomingMessages(id, command);
        }
    }
}
