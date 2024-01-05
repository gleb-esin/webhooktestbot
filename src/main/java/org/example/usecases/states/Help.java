package org.example.usecases.states;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.interfaceAdapters.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Help implements State {
    MessageService messageService;

    public void execute(Long chatId) {
        String help = "Добро пожаловать!";
        messageService.sendMessageTo(chatId, help);
    }
}
