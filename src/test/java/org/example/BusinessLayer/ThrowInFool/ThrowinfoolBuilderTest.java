package org.example.BusinessLayer.ThrowInFool;

import org.example.BusinessLayer.throwInFool.ThrowInFool;
import org.example.BusinessLayer.throwInFool.ThrowinfoolBuilder;
import org.example.DataLayer.UserEntityRepository;
import org.example.EntityLayer.Player;
import org.example.ServiseLayer.monitors.GameMonitor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ThrowinfoolBuilderTest {
    AutoCloseable closeable;
    @Mock
    GameMonitor gameMonitor;
    @Mock
    UserEntityRepository userEntityRepository;
    int MAX_ALLOWED_PARALLEL_GAMES;
    @Mock
    ApplicationContext applicationContext;
    @Mock
    Semaphore semaphoreMock;
    @Mock
    ExecutorService executorServiceMock;
    @InjectMocks
    ThrowinfoolBuilder throwinfoolBuilder;
    @Mock
    List<Player> players;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        ThrowinfoolBuilder mock = new ThrowinfoolBuilder(gameMonitor, userEntityRepository, applicationContext);
        throwinfoolBuilder = spy(mock);
        MAX_ALLOWED_PARALLEL_GAMES = 2;


    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testBuildGame() throws InterruptedException {
        List<Player> players = Arrays.asList(new Player(1L, "player1"), new Player(2L, "player2"));

        // Mocking behavior for applicationContext.getBean
        ThrowInFool throwInFoolMock = mock(ThrowInFool.class);
        when(applicationContext.getBean(ThrowInFool.class)).thenReturn(throwInFoolMock);

        // Mocking Semaphore and ExecutorService
        when(executorServiceMock.submit((Callable<Object>) any())).thenAnswer(invocation -> {
            Runnable task = invocation.getArgument(0);
            task.run(); // Simulate execution of the submitted task
            return null;
        });

        // Setting up the MAX_ALLOWED_PARALLEL_GAMES value
        MAX_ALLOWED_PARALLEL_GAMES = 1;

        // Mocking behavior for semaphore.acquire() and semaphore.release()
        doNothing().when(semaphoreMock).acquire();
        doNothing().when(semaphoreMock).release();

        // Calling the method to be tested
        throwinfoolBuilder.buildGame(players);

        Thread.sleep(1000);
        // Verifying that the gameMonitor.addGame and finnishGame methods were called with the correct arguments
        verify(gameMonitor, times(1)).addGame(any(), eq(players));
        verify(throwInFoolMock, times(1)).play(players);
        verify(throwinfoolBuilder, times(1)).finnishGame(players, any());
        verify(gameMonitor, times(1)).removeGame(any());
        players.forEach(player -> verify(userEntityRepository, times(1)).save(player.toUserEntity()));

        // Verifying that semaphore.acquire() and semaphore.release() were called once each
        verify(semaphoreMock, times(1)).acquire();
        verify(semaphoreMock, times(1)).release();
    }

    @Test
    void finnishGame() {
    }
}