package org.example.interfaceAdapters.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.entities.Player;
import org.example.interfaceAdapters.factories.PlayerFactory;
import org.example.interfaceAdapters.monitor.MessageMonitor;
import org.example.interfaceAdapters.monitor.AbstractMonitor;
import org.example.usecases.states.Help;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UpdateHandler implements ClientCommandHandler {
    PlayerFactory playerfactory;
    MessageMonitor messageMonitor;
    MessageService messageService;
    AbstractMonitor abstractMonitor;


    @Override
    public void handleCommand(Long id, String command) {
        switch (command) {
            case "/start", "/help" -> new Help(messageService).execute(id);
            case "/throwinfool", "/transferfool" -> {
                ExecutorService executorService = Executors.newCachedThreadPool();
                executorService.submit(() -> {
                    Player player = playerfactory.createPlayer(id);
                    abstractMonitor.addPlayer(player, command.substring(1));
                });
                executorService.shutdown();
            }
            default -> messageMonitor.addRequestToIncomingMessages(id, command);
        }
    }
}
