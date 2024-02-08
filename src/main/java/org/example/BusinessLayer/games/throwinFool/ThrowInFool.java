package org.example.BusinessLayer.games.throwinFool;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.BusinessLayer.controller.DeckController;
import org.example.BusinessLayer.controller.PlayerController;
import org.example.BusinessLayer.controller.TableController;
import org.example.BusinessLayer.games.AbstractFool;
import org.example.BusinessLayer.games.Game;
import org.example.BusinessLayer.move.Attack;
import org.example.BusinessLayer.move.Defence;
import org.example.BusinessLayer.move.Throw;
import org.example.BusinessLayer.states.State;
import org.example.EntityLayer.Player;
import org.example.ServiseLayer.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("throwinfool")
@Scope("prototype")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ThrowInFool extends AbstractFool implements State, Game {

    @Autowired
    public ThrowInFool(PlayerController playerController, DeckController deckController, TableController tableController, MessageService messageService, Attack attack, Defence defence, Throw throwMove) {
        super(playerController, deckController, tableController, messageService, attack, defence, throwMove);
    }

    @Override
    public void play(List<Player> players) {
        init(players);
        while (gameIsNotOver()) {
            boolean isAttackerIsWin = attack();
            if (isAttackerIsWin) break;
            boolean isDefenderIsWin = defence();
            if (isDefenderIsWin) break;
            boolean isThrowerIsWin = throwMove();
            if (isThrowerIsWin) break;
            binderGrabsCards(players);
            clearTable();
            fillUpPlayerHands(players);
            changeTurn();
        }
        sendMessageWithGameWinner(players);
    }
}