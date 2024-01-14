package org.example.BusinessLayer.ThrowInFool;

import org.example.BusinessLayer.controller.DeckController;
import org.example.BusinessLayer.controller.PlayerController;
import org.example.BusinessLayer.controller.TableController;
import org.example.BusinessLayer.throwInFool.ThrowInFool;
import org.example.EntityLayer.Player;
import org.example.ServiseLayer.services.MessageService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ThrowInFoolTest {
    AutoCloseable closeable;
    @Mock
    UUID gameID;
    @Mock
    PlayerController playerController;
    @Mock
    DeckController deckController;
    @Mock
    TableController tableController;
    @Mock
    MessageService messageService;
    @InjectMocks
    ThrowInFool throwInFool;
    Player first;
    Player second;
    List<Player> players;

    @BeforeEach
    void setUp() {
        closeable = org.mockito.MockitoAnnotations.openMocks(this);
        first = new Player(1L, "first");
        second = new Player(2L, "second");
        players = List.of(first, second);
        when(playerController.getPlayers()).thenReturn(players);


    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
}