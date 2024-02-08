package org.example.BusinessLayer.games.transferFool;

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
@Component("transferfoolPlayerMonitor")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransferFoolPlayerMonitor extends AbstractPlayerMonitor implements PlayersMonitor {
    @Autowired
    public TransferFoolPlayerMonitor(GameFactory gameFactory, MessageService messageService) {
        super(gameFactory, messageService);
    }
}