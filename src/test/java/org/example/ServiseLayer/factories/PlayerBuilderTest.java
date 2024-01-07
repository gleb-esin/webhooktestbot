package org.example.ServiseLayer.factories;

import org.example.DataLayer.UserEntityRepository;
import org.example.EntityLayer.Player;
import org.example.EntityLayer.UserEntity;
import org.example.ServiseLayer.services.MessageService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PlayerBuilderTest {
    AutoCloseable closeable;
    @Mock
    UserEntityRepository userEntityRepository;
    @Mock
    MessageService messageService;
    Long chatId;
    String notification;
    String confirmation;
    String warning;
    String playerStats;
    String welcomeBack;
    UserEntity userEntity;
    @InjectMocks
    PlayerBuilder playerBuilder;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        chatId = 123L;
        userEntity = new UserEntity(chatId, "Test Name");
        when(messageService.receiveMessageFrom(123L)).thenReturn("Test Name");
        notification = "Для участия в игре нужно зарегистрироваться. Выберите ваше имя в игре: ";
        confirmation = "Test Name, Вы успешно зарегистрированы в игре!";
        warning = "Такое имя уже занято. Пожалуйста, выберите другое: ";
        playerStats = "Test Stats";
        welcomeBack = "С возвращением, Test Name!\n" + playerStats;
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void createPlayer_whenPlayerIsNotRegisteredAndNameIsNotTaken_thenNotificationsAndConfirmationAreSentAndPlayerIsCreatedAndSaved() {
        when(userEntityRepository.existsByUserId(123L)).thenReturn(false);
        when(userEntityRepository.existsByName("Test Name")).thenReturn(false);
        Player player = playerBuilder.createPlayer(123L);

        assertEquals("<b>Test Name</b>", player.getName());
        assertEquals(123L, player.getChatID());
        verify(messageService).sendMessageTo(123L, notification);
        verify(messageService).sendMessageTo(123L, confirmation);
        verify(userEntityRepository).save(any());
    }

    @Test
    void createPlayer_whenPlayerIsNotRegisteredAndNameIsTaken_thenNotificationWarningAndConfirmationAreSentAndPlayerIsCreatedAndSaved() {
        when(userEntityRepository.existsByUserId(123L)).thenReturn(false);
        when(userEntityRepository.existsByName("Test Name")).thenReturn(true).thenReturn(false);
        Player player = playerBuilder.createPlayer(123L);

        assertEquals("<b>Test Name</b>", player.getName());
        assertEquals(123L, player.getChatID());
        verify(messageService).sendMessageTo(123L, notification);
        verify(messageService).sendMessageTo(123L, warning);
        verify(messageService).sendMessageTo(123L, confirmation);
        verify(userEntityRepository).save(any());
    }

    @Test
    void createPlayer_whenPlayerRegistered_() {
        when(userEntityRepository.existsByUserId(123L)).thenReturn(true);
        when(userEntityRepository.findByUserId(123L)).thenReturn(userEntity);

        Player player = playerBuilder.createPlayer(123L);

        assertEquals("<b>Test Name</b>", player.getName());
        assertEquals(123L, player.getChatID());
        verify(messageService, never()).sendMessageTo(123L, welcomeBack);
    }
}