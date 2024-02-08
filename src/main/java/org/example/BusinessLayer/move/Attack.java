package org.example.BusinessLayer.move;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.BusinessLayer.controller.PlayerController;
import org.example.BusinessLayer.controller.TableController;
import org.example.EntityLayer.Card;
import org.example.EntityLayer.Player;
import org.example.ServiseLayer.services.MessageService;
import org.example.ServiseLayer.services.PlayerInputValidator;
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
        String message = "⚔️ Ход " +
                attacker.getName() +
                " под " +
                defender.getName() +
                "⚔️" +
                System.lineSeparator() +
                "Козырь: " +
                tableController.getTable().getTrump();

        messageService.sendMessageToAll(playerController.getPlayers(), message);
        messageService.sendMessageTo(attacker, attacker.toString());
    }

    public void move(PlayerController playerController, TableController tableController) {
        Player attacker = playerController.getAttacker();
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
