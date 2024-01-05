package org.example.usecases.move;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.usecases.controller.PlayerController;
import org.example.usecases.controller.TableController;
import org.example.entities.Card;
import org.example.entities.Player;
import org.example.interfaceAdapters.service.MessageService;
import org.example.interfaceAdapters.service.PlayerInputValidator;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Attack {
    MessageService messageService;
    PlayerInputValidator playerInputValidator;


    public void init(PlayerController playerController, TableController tableController) {
        Player attacker = playerController.getAttacker();
        Player defender = playerController.getDefender();
        StringBuilder message = new StringBuilder("⚔️ Ход ")
                .append(attacker.getName())
                .append(" под ")
                .append(defender.getName())
                .append("⚔️")
                .append(System.lineSeparator())
                .append(tableController.getTable().toString());
        messageService.sendMessageToAll(playerController.getPlayers(), message.toString());
        messageService.sendMessageTo(attacker, attacker.toString());
    }

    public void move(Player attacker, TableController tableController, PlayerController playerController) {
        List<Card> cards = playerInputValidator.askForCards(attacker);
        boolean isMoveCorrect = isAttackMoveCorrect(cards);
        while (cards.isEmpty() || (cards.size() > 1 && !isMoveCorrect)) {
            messageService.sendMessageTo(attacker, "Так пойти не получится.");
            cards = playerInputValidator.askForCards(attacker);
            isMoveCorrect = isAttackMoveCorrect(cards);
        }
        tableController.addCardsToTable(cards, attacker);
        messageService.sendMessageToAll(playerController.getPlayers(), tableController.getTable().toString());
        attacker.setRole("thrower");
    }

    private boolean isAttackMoveCorrect(List<Card> cards) {
        boolean isMoveCorrect = true;
        for (int i = 0; i < cards.size() - 1; i++) {
            isMoveCorrect = cards.get(i).getValue().equals(cards.get(i + 1).getValue());
            if (!isMoveCorrect) break;
        }
        return isMoveCorrect;
    }

}