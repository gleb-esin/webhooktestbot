package org.example.controller.move;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.controller.PlayerController;
import org.example.controller.PlayerInputValidator;
import org.example.controller.TableController;
import org.example.model.Card;
import org.example.model.Player;
import org.example.network.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.example.controller.moveValidator.AttackValidator.isAttackMoveCorrect;
@Component
@AllArgsConstructor(onConstructor_ = {@Autowired})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Attack extends PlayerInputValidator implements MoveInterface {
    TelegramBot bot;

    public void init(PlayerController playerController, TableController tableController) {

        Player attacker = playerController.getAttacker();
        Player defender = playerController.getDefender();
        bot.sendNotificationToAll(playerController.getPlayers(), "==============================\n" +
                "Ход " + attacker.getName() + " под " + defender.getName()+
                "\n"+tableController.getTable());
        bot.sendNotificationTo(attacker, attacker.toString());
    }
    @Override
    public void move(Player attacker, List<Player> playersForNotify, TableController tableController) {
        List<Card> cards = askForCards(attacker);
        boolean isMoveCorrect = isAttackMoveCorrect(cards);
        while (cards.isEmpty() || (cards.size() > 1 && !isMoveCorrect)) {
            bot.sendNotificationTo(attacker, "Так пойти не получится.");
            cards = askForCards(attacker);
            isMoveCorrect = isAttackMoveCorrect(cards);
        }
        tableController.addCardsToTable(cards, attacker);
        attacker.setRole("thrower");
    }
}
