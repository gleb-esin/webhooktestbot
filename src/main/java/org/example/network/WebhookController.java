package org.example.network;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class WebhookController {
    private final TelegramBot bot;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<BotApiMethod<?>> onUpdateReceived(@RequestBody Update update) {
        BotApiMethod<?> response = bot.onWebhookUpdateReceived(update);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

//    @RequestMapping(value = "/", method = RequestMethod.POST)
//    public ResponseEntity<BotApiMethod<?>> onUpdateReceived(@RequestBody Update update) {
//        return new ResponseEntity<>(new SendMessage(update.getMessage().getChatId().toString(), "OK"), HttpStatus.OK);
//    }
}
