package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

@Configuration
public class AppConfig {

    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url("${telegrambot.webHookPath}").build();
    }
}
