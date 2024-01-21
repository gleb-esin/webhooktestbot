package org.example.BusinessLayer.ThrowInFool;

import org.example.BusinessLayer.throwInFool.ThrowInFool;
import org.example.BusinessLayer.throwInFool.ThrowinfoolBuilder;
import org.example.DataLayer.UserEntityRepository;
import org.example.EntityLayer.Player;
import org.example.EntityLayer.UserEntity;
import org.example.ServiseLayer.monitors.GameMonitor;
import org.example.ServiseLayer.services.MessageService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

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
    Semaphore semaphore;
    @Mock
    ThrowInFool throwInFool;
    @Mock
    MessageService messageService;
    ThrowinfoolBuilder throwinfoolBuilder;
    @Mock
    List<Player> players;
    Player attacker;
    Player defender;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        MAX_ALLOWED_PARALLEL_GAMES = 2;
        semaphore = new Semaphore(MAX_ALLOWED_PARALLEL_GAMES);
        ThrowinfoolBuilder mock = new ThrowinfoolBuilder(gameMonitor, userEntityRepository, applicationContext, semaphore, messageService);
        throwinfoolBuilder = spy(mock);
        attacker = new Player(1L, "player1");
        defender = new Player(2L, "player2");
        players = Arrays.asList(attacker, defender);
        when(applicationContext.getBean(ThrowInFool.class)).thenReturn(throwInFool);

    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void buildGame_whenCalled_gameMonitorAddGameIsCalled() {
        throwinfoolBuilder.buildGame(players);

        verify(gameMonitor, times(1)).addGame(any(), eq(players));
    }

    @Test
    void buildGame_whenCalled_throwInFoolPlayIsCalled() {
        throwinfoolBuilder.buildGame(players);

        verify(throwInFool, times(1)).play(players);
    }

    @Test
    void buildGame_whenCalled_thenThrowinfoolBuilderFinnishIsCalled() {
        throwinfoolBuilder.buildGame(players);

        verify(throwinfoolBuilder, times(1)).finnishGame(eq(players), any());
    }

    @Test
    void finnishGame_whenCalled_gameMonitorRemoveGameIsCalled() {
        UUID gameID = UUID.fromString("b68087a7-1f51-4ce9-8357-4b902d2abadb");
        throwinfoolBuilder.finnishGame(players, gameID);
        gameMonitor.removeGame(gameID);
    }

    @Test
    void finnishGame_whenCalled_thenSaveUserEntities() {
        UUID gameID = UUID.fromString("b68087a7-1f51-4ce9-8357-4b902d2abadb");

        throwinfoolBuilder.finnishGame(players, gameID);

        verify(userEntityRepository, atLeastOnce()).save(any(UserEntity.class));
    }

    @Test
    void runGame_whenCalled_thenBuildGameInvoked() {
        List<Player> firstPlayers = mock(List.class);

        CompletableFuture.runAsync(() -> {
            throwinfoolBuilder.runGame(firstPlayers);
        });
    }

    @Test
    void runGame_whenCalledAndServersWasBusy_thenSendSendBusyServersWarningAndAvailabilityNotification() throws InterruptedException {
        List<Player> firstPlayers = mock(List.class);
        List<Player> secondPlayers = mock(List.class);
        List<Player> thirdPlayers = mock(List.class);
        doAnswer(invocation -> {
            Thread.sleep(1000);
            return null;
        }).when(throwinfoolBuilder).buildGame(anyList());
        String busyServersWarning = "Все игровые сервера заняты, подождите, пожалуйста";
        String availabilityNotification = "Добро пожаловать в игру!";
        CountDownLatch countDownLatch = new CountDownLatch(3);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);
        // Задача для первого потока
        Runnable task1 = () -> {
            throwinfoolBuilder.runGame(firstPlayers);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            throw new RuntimeException(e);
            }
            countDownLatch.countDown();
        };

        // Задача для второго потока
        Runnable task2 = () -> {
            throwinfoolBuilder.runGame(secondPlayers);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            countDownLatch.countDown();
        };
        // Задача для третьего потока
        Runnable task3 = () -> {
            throwinfoolBuilder.runGame(thirdPlayers);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            countDownLatch.countDown();
        };
        // Запуск задач с задержкой
        scheduler.schedule(task1, 0, TimeUnit.MILLISECONDS);
        scheduler.schedule(task2, 300, TimeUnit.MILLISECONDS);
        scheduler.schedule(task3, 600, TimeUnit.MILLISECONDS);

        // Ожидание завершения выполнения всех задач
        countDownLatch.await();
        scheduler.shutdown();
        verify(throwinfoolBuilder, times(3)).buildGame(anyList());
        verify(messageService, times(1)).sendMessageToAll(eq(thirdPlayers), eq(busyServersWarning));
        verify(messageService, times(1)).sendMessageToAll(eq(thirdPlayers), eq(availabilityNotification));
    }
}