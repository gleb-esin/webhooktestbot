package org.example.interfaceAdapters.monitor;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.example.entities.Player;
import org.example.interfaceAdapters.factories.AbstractGameFactory;
import org.example.interfaceAdapters.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ThrowinfoolMonitor extends Monitor {
    AbstractGameFactory abstractGameFactory;
    MessageService messageService;
    ConcurrentLinkedQueue<Player> throwInFoolWaiters = new ConcurrentLinkedQueue<>();
    @Value("${game.maxPlayers}")
    @NonFinal
    int MAX_PLAYERS;

    @Autowired
    public ThrowinfoolMonitor(AbstractGameFactory abstractGameFactory, MessageService messageService) {
        this.abstractGameFactory = abstractGameFactory;
        this.messageService = messageService;
    }

    @Override
    public void addPlayer(Player player, String command) {
        throwInFoolWaiters.add(player);
        runGameIfPlayersEquals(MAX_PLAYERS, player, command);
    }

    @Override
    protected void runGameIfPlayersEquals(int MAX_PLAYERS, Player player, String command) {
        if (getQueueSize() == MAX_PLAYERS) {
            abstractGameFactory.create(command, getPlayers());

        } else {
            messageService.sendMessageTo(player.getChatID(), "Ждем игроков");
        }
    }

    @Override
    public List<Player> getPlayers() {
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < MAX_PLAYERS; i++) {
            players.add(throwInFoolWaiters.poll());
        }
        return players;
    }

    @Override
    public int getQueueSize() {
        return throwInFoolWaiters.size();
    }
}
