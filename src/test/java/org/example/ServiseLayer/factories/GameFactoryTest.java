package org.example.ServiseLayer.factories;

import org.example.BusinessLayer.throwInFool.ThrowinfoolBuilder;
import org.example.EntityLayer.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class GameFactoryTest {
    AutoCloseable closeable;
    Map<String, GameBuilder> gameFactories;
    @Mock
    ThrowinfoolBuilder throwinfoolBuilder;
    @Mock
    List<Player> players;
    GameFactory gameFactory;


    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        gameFactories = new HashMap<>();
        gameFactories.put("throwinfoolBuilder", throwinfoolBuilder);
        gameFactory = new GameFactory(gameFactories);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }


    @Test
    void create_whenGameTypeIsCorrect_thenBuildGameIsInvoked() {
        gameFactory.create("throwinfool", players);
        verify(throwinfoolBuilder).runGame(eq(players));
    }

    @Test
    void create_whenGameTypeIsNotCorrect_thenBuildGameIsNotInvoked() {
        gameFactory.create("noSuchGame", players);
        verify(throwinfoolBuilder, never()).runGame(players);
    }
}