package org.example.DataLayer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.concurrent.Semaphore;

@Configuration
public class AppConfig {
    @Bean
    @Lazy
    public Semaphore Semaphore() {
        return new Semaphore(2);
    }
}
