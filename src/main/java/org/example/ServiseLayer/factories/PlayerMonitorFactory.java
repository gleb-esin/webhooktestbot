package org.example.ServiseLayer.factories;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.EntityLayer.Player;
import org.example.ServiseLayer.monitors.PlayersMonitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Factory class for creating and managing player monitors.
 */
@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PlayerMonitorFactory {
    Map<String, PlayersMonitor> monitorMap;

    /**
     * Adds a player to the specified game type monitor.
     *
     * @param player    The player to add.
     * @param gameType  The game type for which the player should be added.
     */
    public void addPlayer(Player player, String gameType) {
        String key = gameType + "PlayerMonitor";
        if (monitorMap.containsKey(key)) {
            monitorMap.get(key).addPlayer(gameType, player);
        } else {
            log.error("PlayerMonitorFactory.addPlayer: Monitor for game type {} not found" + gameType);
        }
    }
}

