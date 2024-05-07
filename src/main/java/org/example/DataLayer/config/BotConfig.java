package org.example.DataLayer.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class BotConfig {
    @Value("${webHookPath}")
    String webHookPath;
    @Value("${botName}")
    String userName;
    @Value("${botToken}")
    String botToken;
}


