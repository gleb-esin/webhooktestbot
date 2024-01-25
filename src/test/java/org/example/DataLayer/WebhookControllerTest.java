package org.example.DataLayer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class WebhookControllerTest {
    AutoCloseable closeable;
    MockMvc mockMvc;

    Update update;
    @Mock
    TelegramBot bot;
    @InjectMocks
    WebhookController webhookController;
    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(webhookController).build();
        update = new Update();
    }
    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
    @Test
    void onUpdateReceived() throws Exception {
        String requestBody = new ObjectMapper().writeValueAsString(update);

        mockMvc.perform(post("/webhook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        verify(bot).onWebhookUpdateReceived(update);
    }
}