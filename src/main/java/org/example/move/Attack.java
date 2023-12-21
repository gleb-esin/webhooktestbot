package org.example.move;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.controller.PlayerController;
import org.example.network.TelegramBot;
import org.example.controller.TableController;
import org.example.model.Card;
import org.example.model.Player;

import java.util.List;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Attack implements Move {
    TelegramBot bot;

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
        sendMessageToAll(playerController.getPlayers(), message.toString(), bot);
        sendMessageTo(attacker, attacker.toString(), bot);
    }

    public void move(Player attacker, TableController tableController, PlayerController playerController) {
        List<Card> cards = askForCards(attacker, bot);
        boolean isMoveCorrect = isAttackMoveCorrect(cards);
        while (cards.isEmpty() || (cards.size() > 1 && !isMoveCorrect)) {
            sendMessageTo(attacker, "Так пойти не получится.", bot);
            cards = askForCards(attacker, bot);
            isMoveCorrect = isAttackMoveCorrect(cards);
        }
        tableController.addCardsToTable(cards, attacker);
        sendMessageToAll(playerController.getPlayers(), tableController.getTable().toString(), bot);
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
