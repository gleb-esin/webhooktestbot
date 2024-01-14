package org.example.BusinessLayer.ThrowInFool;

import org.example.BusinessLayer.controller.DeckController;
import org.example.BusinessLayer.controller.PlayerController;
import org.example.BusinessLayer.controller.TableController;
import org.example.BusinessLayer.throwInFool.ThrowInFool;
import org.example.ServiseLayer.services.MessageService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.UUID;

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

    @BeforeEach
    void setUp() {
        closeable = org.mockito.MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
}