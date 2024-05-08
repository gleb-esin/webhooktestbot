package org.example.BusinessLayer.games;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.example.BusinessLayer.controller.DeckController;
import org.example.BusinessLayer.controller.PlayerController;
import org.example.BusinessLayer.controller.TableController;
import org.example.BusinessLayer.move.Attack;
import org.example.BusinessLayer.move.Defence;
import org.example.BusinessLayer.move.Throw;
import org.example.EntityLayer.Player;
import org.example.ServiseLayer.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class AbstractFool {
    PlayerController playerController;
    DeckController deckController;
    TableController tableController;
    MessageService messageService;
    Attack attack;
    Defence defence;
    Throw throwMove;
    @NonFinal
    boolean isDeckIsEmptyMessageNotSent = true;

    @Autowired
    protected AbstractFool(PlayerController playerController, DeckController deckController, TableController tableController, MessageService messageService, Attack attack, Defence defence, Throw throwMove) {
        this.playerController = playerController;
        this.deckController = deckController;
        this.tableController = tableController;
        this.messageService = messageService;
        this.attack = attack;
        this.defence = defence;
        this.throwMove = throwMove;
    }

    protected void sendMessageWithGameWinner(List<Player> players) {
        messageService.sendMessageToAll(players, "\uD83C\uDFC6 Победил " + playerController.getWinner().getName() + "! \uD83C\uDFC6");
    }

    public abstract void play(List<Player> players);

    protected void changeTurn() {
        playerController.changeTurn();
    }

    protected void clearTable() {
        tableController.clear();
    }

    protected boolean gameIsNotOver() {
        return !playerController.isGameOver();
    }

    protected void fillUpPlayerHands(List<Player> players) {
        if (!deckController.getDeck().isEmpty()) {
            deckController.fillUpTheHands(playerController.getThrowQueue(), playerController.getDefender());
        } else {
            if (isDeckIsEmptyMessageNotSent) {
                messageService.sendMessageToAll(players, "\uD83D\uDE45 <b>Колода пуста!</b> \uD83D\uDE45");
                isDeckIsEmptyMessageNotSent = false;
            }
        }
    }

    protected void binderGrabsCards(List<Player> players) {
        if (playerController.getDefender().getRole().equals("binder")) {
            playerController.setBinder(playerController.getDefender());
            messageService.sendMessageToAll(players, playerController.getBinder().getName() + " забирает карты " + tableController.getAll().toString().substring(1, tableController.getAll().toString().length() - 1));
            playerController.getBinder().getPlayerHand().addAll(tableController.getAll());
        }
    }

    protected boolean throwMove() {
        for (Player thrower : playerController.getThrowQueue()) {
            boolean throwIsPossible = throwMove.isThrowPossible(tableController.getAll(), thrower, playerController.getDefender());
            while (throwIsPossible) {
                boolean isDefenceNeeded = throwMove.move(thrower, playerController, tableController, deckController);
                if (playerController.isPlayerWinner(thrower, deckController.getDeck())) return true;
                if (isDefenceNeeded) {
                    defence.init(playerController);
                    defence.move(playerController, tableController);
                    if (playerController.isPlayerWinner(playerController.getDefender(), deckController.getDeck()))
                        return true;
                }
                throwIsPossible = throwMove.isThrowPossible(tableController.getAll(), thrower, playerController.getDefender());
            }
        }
        return false;
    }

    protected boolean defence() {
        defence.init(playerController);
        defence.move(playerController, tableController);
        return playerController.isPlayerWinner(playerController.getDefender(), deckController.getDeck());
    }

    protected boolean attack() {
        attack.init(playerController, tableController);
        attack.move(playerController, tableController);
        return playerController.isPlayerWinner(playerController.getAttacker(), deckController.getDeck());
    }

    protected void init(List<Player> players) {
        players.forEach(player -> {
            deckController.fillUpThePlayersHand(player);
            player.setGames(player.getGames() + 1);
        });
        playerController.setPlayers(players);
        playerController.setPlayersTurn();
        tableController.setTrump(deckController.getTrump());
    }
}
