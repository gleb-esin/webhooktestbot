package org.example.BusinessLayer.games;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.BusinessLayer.controller.DeckController;
import org.example.BusinessLayer.controller.PlayerController;
import org.example.BusinessLayer.controller.TableController;
import org.example.BusinessLayer.move.Attack;
import org.example.BusinessLayer.move.DefenceForTransferFool;
import org.example.BusinessLayer.move.Throw;
import org.example.BusinessLayer.states.State;
import org.example.EntityLayer.Card;
import org.example.EntityLayer.Player;
import org.example.ServiseLayer.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("transferfool")
@Scope("prototype")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransferFool extends AbstractFool implements State, Game {
    PlayerController playerController;
    TableController tableController;

    @Autowired
    public TransferFool(PlayerController playerController, DeckController deckController, TableController tableController, MessageService messageService, Attack attack, DefenceForTransferFool defence, Throw throwMove) {
        super(playerController, deckController, tableController, messageService, attack, defence, throwMove);
        this.playerController = playerController;
        this.tableController = tableController;
    }

    @Override
    public void play(List<Player> players) {
        init(players);
        while (gameIsNotOver()) {
            boolean isAttackerIsWin = attack();
            if (isAttackerIsWin) break;
            boolean isDefenderIsWin = defence();
            if (isDefenderIsWin) break;
            while (isDefenceIsTransfer()) {
                changeDefender();
                isDefenderIsWin = defence();
                if (isDefenderIsWin) break;
            }
            boolean isThrowerIsWin = throwMove();
            if (isThrowerIsWin) break;
            binderGrabsCards(players);
            clearTable();
            fillUpPlayerHands(players);
            changeTurn();
        }
        sendMessageWithGameWinner(players);
    }

    protected void changeDefender() {
        Player nextDefender;
        if (playerController.getThrowQueue().size() == 1) {
            nextDefender = playerController.getThrowQueue().pop();
        } else {
            nextDefender = playerController.getThrowQueue().get(1);
            playerController.getThrowQueue().remove(1);
        }
        playerController.setThrower(playerController.getDefender());
        playerController.setDefender(nextDefender);
    }

    protected boolean isDefenceIsTransfer() {
        boolean playerIsNotBinder = !playerController.getDefender().getRole().equals("binder");
        boolean tableIsNotEmpty = !tableController.getTable().getUnbeatenCards().isEmpty();
        boolean cardsValuesAreEqual = true;
        Card firstCard = tableController.getTable().getUnbeatenCards().get(0);
        for (int i = 1; i < tableController.getTable().getUnbeatenCards().size(); i++) {
            Card nextCard = tableController.getTable().getUnbeatenCards().get(i);
            if (!nextCard.getValue().equals(firstCard.getValue())) {
                cardsValuesAreEqual = false;
                break;
            }
        }
        return playerIsNotBinder && cardsValuesAreEqual && tableIsNotEmpty;
    }
}