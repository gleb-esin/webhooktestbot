package org.example.BusinessLayer.games;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.ServiseLayer.factories.GameFactory;
import org.example.ServiseLayer.monitors.PlayersMonitor;
import org.example.ServiseLayer.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("transferfoolPlayerMonitor")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransferFoolPlayerMonitor extends AbstractPlayerMonitor implements PlayersMonitor {
    @Autowired
    public TransferFoolPlayerMonitor(GameFactory gameFactory, MessageService messageService) {
        super(gameFactory, messageService);
    }
}