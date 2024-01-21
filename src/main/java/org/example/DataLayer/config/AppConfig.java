package org.example.DataLayer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

import java.util.concurrent.Semaphore;

@Configuration
@PropertySource("classpath:telegram.properties")
@PropertySource("classpath:db.properties")
public class AppConfig {

    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url("${telegrambot.webHookPath}").build();
    }

    @Bean
    @Lazy
    public Semaphore Semaphore() {
        return new Semaphore(2);
    }
}
