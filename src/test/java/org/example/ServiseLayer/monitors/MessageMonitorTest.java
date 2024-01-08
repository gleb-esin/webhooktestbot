package org.example.ServiseLayer.monitors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MessageMonitorTest {
    AutoCloseable closeable;
    MessageMonitor messageMonitor;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        messageMonitor = new MessageMonitor();
    }


    @Test
    void requestIncomingMessage() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName());
            return messageMonitor.requestIncomingMessage(1L);
        });
        try {
            Thread.currentThread().sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        messageMonitor.completeRequestedMessage(1L, "test");

        String expected = future.join();
        assertEquals(expected, "test");
    }
}