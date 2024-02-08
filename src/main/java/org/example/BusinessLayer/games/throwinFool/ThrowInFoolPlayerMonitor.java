package org.example.BusinessLayer.games.throwinFool;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.BusinessLayer.games.AbstractPlayerMonitor;
import org.example.ServiseLayer.factories.GameFactory;
import org.example.ServiseLayer.monitors.PlayersMonitor;
import org.example.ServiseLayer.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This class implements PlayersMonitor, and it is responsible for managing players in ThrowInFool queue and running a ThrowInFool game if the number of players is equal to or greater than the maximum number of players.
 */
@Component("throwinfoolPlayerMonitor")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ThrowInFoolPlayerMonitor extends AbstractPlayerMonitor implements PlayersMonitor {

    /**
     * Creates a new ThrowinfoolMonitor instance.
     *
     * @param gameFactory The abstract game factory to create games.
     * @param messageService      The message service to send messages.
     */
    @Autowired
    public ThrowInFoolPlayerMonitor(GameFactory gameFactory, MessageService messageService) {
        super(gameFactory, messageService);
    }

}