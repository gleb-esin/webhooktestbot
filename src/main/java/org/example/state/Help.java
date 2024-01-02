package org.example.state;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Help {
    Long chatId;
    MessageService messageService;

    public void execute() {
        String help = "Добро пожаловать!";
        messageService.sendMessageTo(chatId, help);
    }
}
