package org.example.BusinessLayer.throwInFool;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.example.DataLayer.UserEntityRepository;
import org.example.EntityLayer.Player;
import org.example.ServiseLayer.factories.GameBuilder;
import org.example.ServiseLayer.monitors.GameMonitor;
import org.example.ServiseLayer.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Semaphore;

@Slf4j
@Component("throwinfoolBuilder")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ThrowinfoolBuilder implements GameBuilder {
    GameMonitor gameMonitor;
    UserEntityRepository userEntityRepository;
    ApplicationContext applicationContext;
    @NonFinal
    volatile Semaphore semaphore;
    MessageService messageService;
    @NonFinal
    volatile boolean serversAreBusy = false;

    @Autowired
    public ThrowinfoolBuilder(GameMonitor gameMonitor, UserEntityRepository userEntityRepository, ApplicationContext applicationContext, Semaphore semaphore, MessageService messageService) {
        this.gameMonitor = gameMonitor;
        this.userEntityRepository = userEntityRepository;
        this.applicationContext = applicationContext;
        this.semaphore = semaphore;
        this.messageService = messageService;
    }

    @Override
    public void buildGame(List<Player> players) {
        UUID gameID = UUID.randomUUID();
        gameMonitor.addGame(gameID, players);
        ThrowInFool throwInFool = applicationContext.getBean(ThrowInFool.class);
        throwInFool.play(players);
        finnishGame(players, gameID);

    }

    @Override
    public void finnishGame(List<Player> players, UUID gameID) {
        gameMonitor.removeGame(gameID);
        for (Player player : players) {
            userEntityRepository.save(player.toUserEntity());
        }
    }

    @Override
    public void runGame(List<Player> players) {
        if (semaphore.availablePermits() == 0) {
            serversAreBusy = true;
            messageService.sendMessageToAll(players, "Все игровые сервера заняты, подождите, пожалуйста");
        }
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (serversAreBusy) {
            messageService.sendMessageToAll(players, "Добро пожаловать в игру!");
            serversAreBusy = false;
        }
        buildGame(players);
        semaphore.release();
    }
}
