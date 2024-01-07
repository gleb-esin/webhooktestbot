package org.example.ServiseLayer.factories;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.EntityLayer.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * The AbstractGameFactory class is responsible for creating a game based on the provided game type and list of players.
 */
@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class GameFactory {
    Map<String, GameBuilder> gameFactories;

    /**
     * Creates a new game of the specified type with the given players.
     *
     * @param  gameType   the type of game to create
     * @param  players    the list of players participating in the game
     */
    public void create(String gameType, List<Player> players) {
        if (gameFactories.containsKey(gameType)) {
            String key = gameType + "Factory";
            gameFactories.get(key).buildGame(players);
        } else {
            log.error("GameFactory.create(): Unsupported game type: " + gameType);
        }
    }
}
