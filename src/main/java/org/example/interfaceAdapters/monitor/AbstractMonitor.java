package org.example.interfaceAdapters.monitor;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.entities.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AbstractMonitor {
    Map<String, Monitor> monitorMap;

    public void addPlayer(Player player, String command) {
        String key = command + "Monitor";
        monitorMap.get(key).addPlayer(player, command);
    }
}

