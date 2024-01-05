package org.example.interfaceAdapters.factories;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.entities.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AbstractGameFactory {
    Map<String,GameFactory> gameFactories;

    public void create(String command, List<Player> players) {
        String key = command + "Factory";
        System.out.println("DEBUG: key = " + key);
        System.out.println("DEBUG: monitor = " + gameFactories.get(key));
        gameFactories.get(key).create(players);
    }
}
