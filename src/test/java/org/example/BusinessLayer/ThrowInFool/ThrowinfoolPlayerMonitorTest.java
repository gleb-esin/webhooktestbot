package org.example.BusinessLayer.ThrowInFool;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.EntityLayer.Player;
import org.example.ServiseLayer.factories.GameFactory;
import org.example.BusinessLayer.throwInFool.ThrowinfoolPlayerMonitor;
import org.example.ServiseLayer.services.MessageService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
class ThrowinfoolPlayerMonitorTest {
    AutoCloseable closeable;
    @Mock
    MessageService messageService_EventListener;
    @Mock
    GameFactory gameFactory;
    @Mock
    Player player1;
    @Mock
    Player player2;
    @Mock
    Player player3;
    @Spy
    @InjectMocks
    ThrowinfoolPlayerMonitor throwinfoolMonitor;


    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        try {

            Field MAX_PLAYERS = throwinfoolMonitor.getClass().getDeclaredField("MAX_PLAYERS");
            MAX_PLAYERS.setAccessible(true);
            MAX_PLAYERS.set(throwinfoolMonitor, 2);
        } catch (Exception e) {
            log.error("ThrowinfoolMonitorTest.setUp(): " + e.getMessage());
        }
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void addPlayer_whenMAX_PLAYERS_isEquals2_andAddedOnePlayer_thenQueueSizeEqualsOne() {
        throwinfoolMonitor.addPlayer("test", player1);

        assertEquals(1, throwinfoolMonitor.getQueueSize());
    }

    @Test
    void addPlayer_whenMAX_PLAYERS_isEquals2_andAddedTwoPlayers_thenQueueSizeEqualsZero_andInvokedRunGameIfPlayersEquals() {
        throwinfoolMonitor.addPlayer("test", player1);
        throwinfoolMonitor.addPlayer("test", player2);

        assertEquals(0, throwinfoolMonitor.getQueueSize());
        verify(throwinfoolMonitor, times(1)).runGameIfPlayersEquals(2, player1, "test");
    }

    @Test
    void addPlayer_whenMAX_PLAYERS_isEquals2_andAddedThreePlayer_thenQueueSizeEqualsOne_andInvokedRunGameIfPlayersEquals() {
        throwinfoolMonitor.addPlayer("test", player1);
        throwinfoolMonitor.addPlayer("test", player2);
        throwinfoolMonitor.addPlayer("test", player3);

        assertEquals(1, throwinfoolMonitor.getQueueSize());
        verify(throwinfoolMonitor, times(1)).runGameIfPlayersEquals(2, player1, "test");
    }

    @Test
    void runGameIfPlayersEquals_whenMAX_PLAYERS_isEquals2_andAddedOnePlayer_thenCreateIsNotInvokedAndSendMessageToInvoked() {
        ConcurrentLinkedQueue<Player> mockQueue = new ConcurrentLinkedQueue<>();
        mockQueue.add(player1);

        try {
            Field throwInFoolWaiters = throwinfoolMonitor.getClass().getDeclaredField("throwInFoolWaiters");
            throwInFoolWaiters.setAccessible(true);
            throwInFoolWaiters.set(throwinfoolMonitor, mockQueue);
        } catch (Exception e) {
            log.error("ThrowinfoolMonitorTest.runGameIfPlayersEquals_whenMAX_PLAYERS_isEquals2_andAddedOnePlayer_thenCreateIsNotInvokedAndSendMessageToInvoked(): " + e.getMessage());
        }

        throwinfoolMonitor.runGameIfPlayersEquals(2, player1, "test");


        verify(gameFactory, times(0)).create(eq("test"), any(ArrayList.class));
        verify(messageService_EventListener, times(1)).sendMessageTo(player1.getChatID(), "Ждем игроков");
    }

    @Test
    void runGameIfPlayersEquals_whenMAX_PLAYERS_isEquals2_andAddedTwoPlayers_thenCreateInvokedAndSendMessageToNotInvoked() {
        ConcurrentLinkedQueue<Player> mockQueue = new ConcurrentLinkedQueue<>();
        mockQueue.add(player1);
        mockQueue.add(player2);

        try {
            Field throwInFoolWaiters = throwinfoolMonitor.getClass().getDeclaredField("throwInFoolWaiters");
            throwInFoolWaiters.setAccessible(true);
            throwInFoolWaiters.set(throwinfoolMonitor, mockQueue);
        } catch (Exception e) {
            log.error("ThrowinfoolMonitorTest.runGameIfPlayersEquals_whenMAX_PLAYERS_isEquals2_andAddedTwoPlayers_thenCreateInvokedAndSendMessageToNotInvoked(): " + e.getMessage());
        }

        throwinfoolMonitor.runGameIfPlayersEquals(2, player2, "test");



        verify(gameFactory, times(1)).create(eq("test"), any(ArrayList.class));
        verify(messageService_EventListener, times(0)).sendMessageTo(player2.getChatID(), "Ждем игроков");
    }

    @Test
    void runGameIfPlayersEquals_whenMAX_PLAYERS_isEquals2_andAddedThreePlayers_thenCreateInvokedAndSendMessageToInvoked() {
        ConcurrentLinkedQueue<Player> mockQueue = new ConcurrentLinkedQueue<>();
        mockQueue.add(player1);
        mockQueue.add(player2);
        mockQueue.add(player3);
        try {
            Field throwInFoolWaiters = throwinfoolMonitor.getClass().getDeclaredField("throwInFoolWaiters");
            throwInFoolWaiters.setAccessible(true);
            throwInFoolWaiters.set(throwinfoolMonitor, mockQueue);
        } catch (Exception e) {
            log.error("runGameIfPlayersEquals_whenMAX_PLAYERS_isEquals2_andAddedThreePlayers_thenCreateInvokedAndSendMessageToInvoked(): " + e.getMessage());
        }

        throwinfoolMonitor.runGameIfPlayersEquals(2, player3, "test");

        verify(gameFactory, times(1)).create(eq("test"), any(ArrayList.class));
        verify(messageService_EventListener, times(0)).sendMessageTo(player3.getChatID(), "Ждем игроков");
    }

    @Test
    void getPlayers_whenMAX_PLAYERS_isEquals2_andAddedTwoPlayers_thenListSizeEqualsTwo() {
        ConcurrentLinkedQueue<Player> mockQueue = new ConcurrentLinkedQueue<>();
        mockQueue.add(player1);
        mockQueue.add(player2);

        try {
            Field throwInFoolWaiters = throwinfoolMonitor.getClass().getDeclaredField("throwInFoolWaiters");
            throwInFoolWaiters.setAccessible(true);
            throwInFoolWaiters.set(throwinfoolMonitor, mockQueue);
        } catch (Exception e) {
            log.error("ThrowinfoolMonitorTest.runGameIfPlayersEquals_whenMAX_PLAYERS_isEquals2_andAddedTwoPlayers_thenCreateInvokedAndSendMessageToNotInvoked(): " + e.getMessage());
        }
        List<Player> actual = throwinfoolMonitor.getPlayers();

        assertEquals(2, actual.size());
    }

    @Test
    void getPlayers_whenMAX_PLAYERS_isEquals2_andAddedThreePlayers_thenListSizeEqualsTwo() {
        ConcurrentLinkedQueue<Player> mockQueue = new ConcurrentLinkedQueue<>();
        mockQueue.add(player1);
        mockQueue.add(player2);
        mockQueue.add(player3);

        try {
            Field throwInFoolWaiters = throwinfoolMonitor.getClass().getDeclaredField("throwInFoolWaiters");
            throwInFoolWaiters.setAccessible(true);
            throwInFoolWaiters.set(throwinfoolMonitor, mockQueue);
        } catch (Exception e) {
            log.error("ThrowinfoolMonitorTest.getPlayers_whenMAX_PLAYERS_isEquals2_andAddedTwoPlayers_thenListSizeEqualsTwo(): " + e.getMessage());
        }
        List<Player> actual = throwinfoolMonitor.getPlayers();

        assertEquals(2, actual.size());
    }

    @Test
    void getQueueSize_whenMAX_PLAYERS_isEquals2_andAddedTwoPlayers_thenListSizeEqualsTwo() {
        ConcurrentLinkedQueue<Player> mockQueue = new ConcurrentLinkedQueue<>();
        mockQueue.add(player1);
        mockQueue.add(player2);

        try {
            Field throwInFoolWaiters = throwinfoolMonitor.getClass().getDeclaredField("throwInFoolWaiters");
            throwInFoolWaiters.setAccessible(true);
            throwInFoolWaiters.set(throwinfoolMonitor, mockQueue);
        } catch (Exception e) {
            log.error("ThrowinfoolMonitorTest.getQueueSize_whenMAX_PLAYERS_isEquals2_andAddedTwoPlayers_thenListSizeEqualsTwo(): " + e.getMessage());
        }

        int actual = throwinfoolMonitor.getQueueSize();

        assertEquals(2, actual);
    }
}