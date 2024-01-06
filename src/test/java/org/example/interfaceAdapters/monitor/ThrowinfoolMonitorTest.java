package org.example.interfaceAdapters.monitor;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.entities.Player;
import org.example.interfaceAdapters.factories.AbstractGameFactory;
import org.example.interfaceAdapters.service.MessageService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
class ThrowinfoolMonitorTest {
    AutoCloseable closeable;
    @Mock
    MessageService messageService_EventListener;
    @Mock
    AbstractGameFactory abstractGameFactory;
    @Mock
    Player player1;
    @Mock
    Player player2;
    @Mock
    Player player3;
    @Spy
    @InjectMocks
    ThrowinfoolMonitor throwinfoolMonitor;


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
        throwinfoolMonitor.addPlayer(player1, "test");

        assertEquals(1, throwinfoolMonitor.getQueueSize());
    }

    @Test
    void addPlayer_whenMAX_PLAYERS_isEquals2_andAddedTwoPlayers_thenQueueSizeEqualsZero_andInvokedRunGameIfPlayersEquals() {
        throwinfoolMonitor.addPlayer(player1, "test");
        throwinfoolMonitor.addPlayer(player2, "test");

        assertEquals(0, throwinfoolMonitor.getQueueSize());
        verify(throwinfoolMonitor, times(1)).runGameIfPlayersEquals(2, player1, "test");
    }

    @Test
    void addPlayer_whenMAX_PLAYERS_isEquals2_andAddedThreePlayer_thenQueueSizeEqualsOne_andInvokedRunGameIfPlayersEquals() {
        throwinfoolMonitor.addPlayer(player1, "test");
        throwinfoolMonitor.addPlayer(player2, "test");
        throwinfoolMonitor.addPlayer(player3, "test");

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
        List<Player> players = throwinfoolMonitor.getPlayers();

        verify(abstractGameFactory, times(0)).create(eq("test"), players);
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
        List<Player> players = throwinfoolMonitor.getPlayers();


        verify(abstractGameFactory, times(1)).create(eq("test"), players);
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
        List<Player> players = throwinfoolMonitor.getPlayers();


        verify(abstractGameFactory, times(1)).create(eq("test"), players);
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